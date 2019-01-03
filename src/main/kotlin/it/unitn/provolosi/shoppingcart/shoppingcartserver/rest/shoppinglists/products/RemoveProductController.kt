package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping("/api/shoppingListProducts/{id}")
class RemoveProductController (
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val syncShoppingListService: WebSocketSyncService
) {

    /**
     * Handles the request to remove a product from a list
     */
    @DeleteMapping
    fun removeProduct(
            @AppUser user: User,
            @PathVariable id: Long
    ): ResponseEntity<ShoppingListProduct> = try {
        val relation = shoppingListProductDAO.findById(id)
        val list = relation.shoppingList

        if (list.isUserOwnerOrCollaborator(user)) {

            shoppingListProductDAO.delete(relation)

            syncShoppingListService.productInShoppingListDeleted(relation)

            ok(relation)

        } else {
            forbidden()
        }

    } catch (ex: EntityNotFoundException) {
        notFound()
    }
}