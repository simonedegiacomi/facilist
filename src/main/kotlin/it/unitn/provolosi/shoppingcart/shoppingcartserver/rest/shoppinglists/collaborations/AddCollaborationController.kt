package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import conflict
import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.AddedToListEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.InvitedToJoinEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import ok
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
class AddCollaborationController(
        private val userDAO: UserDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val inviteToJoinDAO: InviteToJoinDAO,
        private val emailService: EmailService,
        private val shoppingListNotifications: ShoppingListNotifications,
        private val syncShoppingListService: WebSocketSyncService
) {

    /**
     * Handles the request to add a new collaborator to a list.
     * Collaborators are added by email address. If a user is already registered with that email address, that user will
     * be notified by notifications and email and also other collaborators of the list will be notified.
     * If no user with the specified email is registered yet, a new email will be sent to that address, inviting the user
     * to register
     */
    @PutMapping()
    @RolesAllowed(User.USER)
    fun addCollaborator(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid @Email emailToAdd: String
    ): ResponseEntity<ShoppingList> = if (list.canUserEditCollaborations(user)) {

        try {
            addUserToShoppingListByEmail(list, user, emailToAdd)

            ok(list)
        } catch (ex: UserAlreadyCollaboratesWithShoppingListException) {
            conflict<ShoppingList>()
        }
    } else {
        forbidden()
    }

    /**
     * Add the user as a collaborator of the lsit if the user is registered, otherwise send an invite to the email address
     */
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

    /**
     * Add the existing user as a collaborator to the list
     */
    private fun addUserToShoppingList(list: ShoppingList, user: User, inviter: User) {
        // Owner of list can't become collaborators
        if (user == list.creator) {
            throw UserAlreadyCollaboratesWithShoppingListException()
        }

        // Create the collaboration
        val collaboration = shoppingListCollaborationDAO.save(ShoppingListCollaboration(
            user = user,
            shoppingList = list
        ))

        // Update the clients
        syncShoppingListService.newCollaborator(collaboration)

        //  Notify the collaborator and the new collaborator
        shoppingListNotifications.notifyNewCollaborator(inviter, collaboration)
        shoppingListNotifications.notifyCollaboratorsNewCollaborator(inviter, collaboration)
        emailService.sendEmail(AddedToListEmail(collaboration, inviter))
    }

    /**
     * Send an invite to the non registered user
     */
    private fun inviteUserByEmailToList(inviter: User, list: ShoppingList, email: String) {
        val invite = inviteToJoinDAO.save(InviteToJoin(
            shoppingList = list,
            email = email,
            inviter = inviter
        ))

        emailService.sendEmail(InvitedToJoinEmail(invite))

        // Update the clients
        syncShoppingListService.newInvite(list, invite)
    }
}