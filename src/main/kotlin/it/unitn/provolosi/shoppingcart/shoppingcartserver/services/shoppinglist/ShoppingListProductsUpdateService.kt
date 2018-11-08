package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductsUpdateDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.RecentShoppingListProductsUpdate
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import org.springframework.stereotype.Component

@Component
class ShoppingListProductsUpdateService (
        private val shoppingListProductsUpdateDAO: ShoppingListProductsUpdateDAO,
) {

    fun collectEvent(user: ShoppingListCollaboration, product: ShoppingListProduct) {
        val list            = product.shoppingList
        val previousUpdate  = shoppingListProductsUpdateDAO.findByShoppingListOrNull(list)
                ?: RecentShoppingListProductsUpdate(list)

        with(previousUpdate) {
            addProduct(product)
            addUser(user)
        }

        shoppingListProductsUpdateDAO.save(previousUpdate)
    }

}