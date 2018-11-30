package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/collaborations")
class DeleteCollaboration(
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val notificationService: NotificationService,
        private val syncShoppingListService: SyncService,

        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    @DeleteMapping("{collaborationId}")
    @RolesAllowed(User.USER)
    fun deleteCollaboration(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @PathVariable collaborationId:Long,
            @AppUser user: User
    ): ResponseEntity<ShoppingList> = try {
        if (list.canUserEditCollaborations(user)) {

            val collaboration = shoppingListCollaborationDAO.findById(collaborationId)

            removeProductsOfUserFromList(collaboration.user, list)

            shoppingListCollaborationDAO.deleteById(collaborationId)

            syncShoppingListService.collaborationDeleted(collaboration)
            sendNotificationToDeletedCollaborator(user, collaboration)
            sendNotificationToCollaborators(user, collaboration)

            ResponseEntity(list, HttpStatus.OK)
        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

    } catch (ex: ShoppingListCollaborationNotFoundException) {
        ResponseEntity.notFound().build()
    }

    private fun sendNotificationToDeletedCollaborator(user: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${user.firstName} ti ha rimosso dai collaboratori di ${list.name}",
            icon    = user.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }

    private fun sendNotificationToCollaborators(user: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val removed = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != user && u != removed }
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha rimosso ${removed.firstName} dai collaboratoridella lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }

    private fun removeProductsOfUserFromList (user: User, list: ShoppingList) {
        val relationsToRemove = list.products
                .filter { relation -> relation.product.creator != null && relation.product.creator == user }

        shoppingListProductDAO.deleteAll(relationsToRemove)

        relationsToRemove
                .forEach { relation -> syncShoppingListService.productInShoppingListDeleted(relation) }
    }
}