package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import notFound
import ok
import org.springframework.beans.factory.annotation.Value
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
            shoppingListNotifications.notifyCollaboratorsCollaborationDeleted(user, collaboration)
            shoppingListNotifications.notifyCollaboratorCollaborationDeleted(user, collaboration)

            ok(list)
        } else {

            forbidden()
        }

    } catch (ex: ShoppingListCollaborationNotFoundException) {
        notFound()
    }

    private fun removeProductsOfUserFromList (user: User, list: ShoppingList) {
        val relationsToRemove = list.products
                .filter { relation -> relation.product.creator != null && relation.product.creator == user }

        shoppingListProductDAO.deleteAll(relationsToRemove)

        relationsToRemove
                .forEach { relation -> syncShoppingListService.productInShoppingListDeleted(relation) }
    }
}