package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import conflict
import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductAlreadyInShoppingListException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/products")
class AddProductController(
        private val productDAO: ProductDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val syncShoppingListService: WebSocketSyncService
) {

    /**
     * Controller for the request to add a product to a list
     */
    @PostMapping
    fun addProduct(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList,
            @RequestBody productId: Long
    ): ResponseEntity<ShoppingListProduct> {
        try {

            val product = productDAO.findById(productId)

            if (!canUserAddProductToList(user, product)) {
                return forbidden()
            }

            // Insert the relation in the database
            val relation = shoppingListProductDAO.save(ShoppingListProduct(
                shoppingList    = list,
                product         = product,
                image           = product.icon,
                bought          = false
            ))

            // Sync the clients
            syncShoppingListService.newShoppingListProduct(relation)

            return ok(relation)

        } catch (ex: ProductNotFoundException) {
            return notFound()
        } catch (ex: ProductAlreadyInShoppingListException) {
            return conflict()
        }
    }

    /**
     * Checks if the specified product can be added to a list by the specified user. So when
     * - The product was created by an admin;
     * - The product was created by the specified user;
     */
    private fun canUserAddProductToList(user: User, product: Product) = product.creator == null || product.creator == user
}