package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.AddedToListEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.InvitedToJoinEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
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
        private val syncShoppingListService: SyncService,

        @Value("\${app.name}")
        private val applicationName: String,


        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun addCollaborator(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid @Email emailToAdd: String
    ): ResponseEntity<ShoppingList> =

        if (list.canUserEditCollaborations(user)) {

            try {
                addUserToShoppingListByEmail(list, user, emailToAdd)

                ResponseEntity(list, HttpStatus.OK)
            } catch (ex: UserAlreadyCollaboratesWithShoppingListException) {
                ResponseEntity.status(HttpStatus.CONFLICT).build<ShoppingList>()
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }


    fun addUserToShoppingListByEmail(
            list: ShoppingList,
            inviter: User,
            email: String
    ) = try {
        val user = userDAO.getUserByEmail(email)
        addUserToShoppingList(list, user, inviter)
    } catch (ex: UserNotFoundException) {
        inviteUserByEmailToList(inviter, list, email)
    }


    private fun addUserToShoppingList(list: ShoppingList, user: User, inviter: User) {
        if (user == list.creator) {
            throw UserAlreadyCollaboratesWithShoppingListException()
        }

        val collaboration = shoppingListCollaborationDAO.save(ShoppingListCollaboration(
            user = user,
            shoppingList = list
        ))

        syncShoppingListService.newCollaborator(collaboration)
        sendNotificationToNewCollaborator(inviter, collaboration)
        sendNotificationToExistingCollaborators(inviter, collaboration)
        emailService.sendEmail(AddedToListEmail(collaboration, inviter))
    }


    private fun sendNotificationToNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ti ha invitato a collaborare alla lista \"${list.name}\"",
            icon    = inviter.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
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
                        icon    = inviter.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }


    private fun inviteUserByEmailToList(inviter: User, list: ShoppingList, email: String) {
        val invite = inviteToJoinDAO.save(InviteToJoin(
            shoppingList = list,
            email = email,
            inviter = inviter
        ))

        emailService.sendEmail(InvitedToJoinEmail(invite))
    }
}