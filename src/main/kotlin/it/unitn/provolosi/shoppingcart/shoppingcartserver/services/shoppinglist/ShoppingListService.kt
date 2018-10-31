package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListCollaborationNotification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.INotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class ShoppingListService(
        private val userDAO: UserDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val inviteToJoinDAO: InviteToJoinDAO,

        private val emailService: EmailService,

        @Value("\${app.name}")
        private val applicationName: String,

        private val syncShoppingListService: ISyncShoppingListService,

        private val notificationService: INotificationService
) : IShoppingListService {

    override fun addUserToShoppingListByEmail(
            list: ShoppingList,
            inviter: User,
            email: String,
            req: HttpServletRequest
    ) {
        if (list.canUserEditCollaborations(inviter)) {

            try {
                val user = userDAO.getUserByEmail(email)
                addUserToShoppingList(list, user, inviter, req)
            } catch (ex: UserNotFoundException) {
                inviteUserByEmailToList(inviter, list, email, req)
            }

        } else {
            throw InviterCantEditCollaboratorsException()
        }
    }

    private fun addUserToShoppingList(list: ShoppingList, user: User, inviter: User, req: HttpServletRequest) {
        val collaboration = shoppingListCollaborationDAO.save(ShoppingListCollaboration(
            user = user,
            shoppingList = list
        ))


        notificationService.saveAndSendCollaboratorNotification(
            collaboration,
            inviter,
            ShoppingListCollaborationNotification.ACTION_ADD_COLLABORATOR
        )
        sendEmailToNewCollaborator(collaboration, req)
    }


    private fun sendEmailToNewCollaborator(
            collaboration: ShoppingListCollaboration,
            req: HttpServletRequest
    ) {
        // TODO: Improve email
        emailService.sendEmail(object : Email() {
            override fun to() = collaboration.user.email

            override fun subject() = "$applicationName - Sei stato aggiunto ad una lista!"

            override fun text() = "Apri la pagina"
        })
    }

    private fun inviteUserByEmailToList(inviter: User, list: ShoppingList, email: String, req: HttpServletRequest) {
        val invite = inviteToJoinDAO.save(InviteToJoin(
            shoppingList = list,
            email = email,
            inviter = inviter
        ))

        sendInviteEmail(invite, req)
    }

    private fun sendInviteEmail(invite: InviteToJoin, req: HttpServletRequest) {
        val inviter = invite.inviter
        val list = invite.shoppingList

        emailService.sendEmail(object : Email() {
            override fun to() = invite.email

            override fun subject() = "Iscriviti a $applicationName!"

            override fun text() = "${inviter.firstName} ti ha invitato a collaborare alla lista ${list.name}. Iscriviti subito!"

            // TODO: Create email template
            /*override fun html() = htmlEmailEngine.render("verify-email", mapOf(
                "applicationName" to applicationName,
                "inviterName" to user.firstName,
                "listName" to list.name,
                "link" to req.protocolPortAndDomain()
            ))*/
        })
    }


    override fun acceptInvitesForUser(user: User, req: HttpServletRequest) = inviteToJoinDAO.findByEmail(user.email).forEach { it ->
        addUserToShoppingList(it.shoppingList, user, it.inviter, req)
    }
}