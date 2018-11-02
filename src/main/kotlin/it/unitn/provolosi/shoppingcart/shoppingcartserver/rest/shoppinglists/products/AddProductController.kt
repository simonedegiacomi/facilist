package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncShoppingListService
import notFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.management.ListenerNotFoundException

@RestController
@RequestMapping("/api/shoppingLists/{id}/products")
class AddProductController (
        private val productDAO: ProductDAO,
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val notificationService: NotificationService,
        private val syncShoppingListService: SyncShoppingListService
) {

    @PostMapping
    fun addProduct(
        @AppUser user: User,
        @PathVariable id: Long,
        @RequestBody productId: Long
    ): ResponseEntity<ShoppingListProduct> {
        try {
            val list = shoppingListDAO.findById(id)
            val product = productDAO.findById(productId)

            if (!list.isUserOwnerOrCollaborator(user)) {
                return forbidden()
            }

            if (product.creator != null && product.creator != user) {
                return forbidden()
            }

            val relation = shoppingListProductDAO.save(ShoppingListProduct(
                shoppingList    = list,
                product         = product,
                image           = product.icon
            ))

            syncShoppingListService.newShoppingListProduct(relation)

            /*notificationService.saveAndSendProductNotification(
                relation,
                user,
                ShoppingListProductNotification.
            )*/

            return ResponseEntity.ok(relation)

        } catch (ex: ListenerNotFoundException) {
            return notFound()
        } catch (ex: ProductNotFoundException) {
            return notFound()
        }
    }
}