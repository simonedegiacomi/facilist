package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Email


@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/collaborations")
class AddCollaboration(
        private val userDAO: UserDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val inviteToJoinDAO: InviteToJoinDAO,
        private val emailService: EmailService,
        private val notificationService: NotificationService,

        @Value("\${app.name}")
        private val applicationName: String
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun addCollaborator(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid @Email emailToAdd: String,
            req: HttpServletRequest
    ): ResponseEntity<ShoppingList> =

        if (list.canUserEditCollaborations(user)) {

            addUserToShoppingListByEmail(list, user, emailToAdd, req)

            ResponseEntity(list, HttpStatus.OK)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }


    fun addUserToShoppingListByEmail(
            list: ShoppingList,
            inviter: User,
            email: String,
            req: HttpServletRequest
    ) = try {
        val user = userDAO.getUserByEmail(email)
        addUserToShoppingList(list, user, inviter, req)
    } catch (ex: UserNotFoundException) {
        inviteUserByEmailToList(inviter, list, email, req)
    }


    private fun addUserToShoppingList(list: ShoppingList, user: User, inviter: User, req: HttpServletRequest) {
        val collaboration = shoppingListCollaborationDAO.save(ShoppingListCollaboration(
            user = user,
            shoppingList = list
        ))


        sendNotificationToNewCollaborator(inviter, collaboration)
        sendNotificationToExistingCollaborators(inviter, collaboration)
        sendEmailToNewCollaborator(collaboration, req)
    }


    private fun sendNotificationToNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ti ha invitato a collaborare alla lista \"${list.name}\"",
            icon    = inviter.photo,
            target  = collaboration.user
        ))
    }

    private fun sendNotificationToExistingCollaborators(inviter: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val invited = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != inviter && u != invited }
                .map { u ->
                    Notification(
                        message = "${inviter.firstName} ha invitato ${invited.firstName} a collaborare alla lista \"${list.name}\"",
                        target  = u,
                        icon    = inviter.photo
                    )
                }

        notificationService.saveAndSend(notifications)
    }



    private fun sendEmailToNewCollaborator(
            collaboration: ShoppingListCollaboration,
            req: HttpServletRequest
    ) {
        // TODO: Improve email
        emailService.sendEmail(object : it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email() {
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

        emailService.sendEmail(object : it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email() {
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
}