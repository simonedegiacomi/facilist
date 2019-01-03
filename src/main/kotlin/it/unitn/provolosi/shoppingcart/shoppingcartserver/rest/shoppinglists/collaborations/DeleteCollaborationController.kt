package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/collaborations")
class DeleteCollaborationController(
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val shoppingListNotifications: ShoppingListNotifications,
        private val syncShoppingListService: WebSocketSyncService
) {

    /**
     * Handler of the request to remove a collaborator from a list.
     * A collaborator of a list can remove himself or other users if he has the proper permission.
     * When a user is removed, the product created by this user are removed from the list.
     * Collaborators will be notified that  the collaborator has been removed.
     */
    @DeleteMapping("{collaborationId}")
    @RolesAllowed(User.USER)
    fun deleteCollaboration(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @PathVariable collaborationId: Long,
            @AppUser user: User
    ): ResponseEntity<ShoppingList> = try {
        val collaboration = shoppingListCollaborationDAO.findById(collaborationId)

        if (!userCanDeleteCollaboration(user, list, collaboration)) {
            forbidden()
        } else {

            removeProductsOfUserFromList(collaboration.user, list)

            // Delete the collaboration
            shoppingListCollaborationDAO.deleteById(collaborationId)

            // update the clients
            syncShoppingListService.collaborationDeleted(collaboration)

            // Notify collaborators
            shoppingListNotifications.notifyCollaboratorsCollaborationDeleted(user, collaboration)

            if (user.id != collaboration.user.id) { // If the user didn't remove himself, notify the ex-collaborator
                shoppingListNotifications.notifyCollaboratorCollaborationDeleted(user, collaboration)
            }

            ok(list)
        }
    } catch (ex: ShoppingListCollaborationNotFoundException) {
        notFound()
    }

    /**
     * Checks if a user can delete a collaboration.
     * Users who can delete a collaborator are:
     * - The owner of the list
     * - Users with the SOCIAL or ADMIN permission
     * - Users that want to leave a list (that is: the user is removing his collaboration)
     */
    private fun userCanDeleteCollaboration(user: User, list: ShoppingList, collaboration: ShoppingListCollaboration): Boolean {
        if (list.creator == user) {
            return true
        }

        val userCollaboration = list.getCollaborationOfUser(user)
        val isUserLeavingList = userCollaboration.id == collaboration.id

        return isUserLeavingList || list.canUserEditCollaborations(user)
    }

    /**
     * Remove products created by the specified user from the specified list
     */
    private fun removeProductsOfUserFromList(user: User, list: ShoppingList) {
        val relationsToRemove = list.products
                .filter { relation -> relation.product.creator == user }

        shoppingListProductDAO.deleteAll(relationsToRemove)

        relationsToRemove
                .forEach { relation -> syncShoppingListService.productInShoppingListDeleted(relation) }
    }
}