package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import notFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.management.ListenerNotFoundException

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/products")
class AddProductController(
        private val productDAO: ProductDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val notificationService: NotificationService,
        private val syncShoppingListService: SyncService
) {

    @PostMapping
    fun addProduct(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList,
            @RequestBody productId: Long
    ): ResponseEntity<ShoppingListProduct> {
        try {

            val product = productDAO.findById(productId)

            if (!list.isUserOwnerOrCollaborator(user)) {
                return forbidden()
            }

            if (product.creator != null && product.creator != user) {
                return forbidden()
            }

            val relation = shoppingListProductDAO.save(ShoppingListProduct(
                shoppingList = list,
                product = product,
                image = product.icon
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