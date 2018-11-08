package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.RecentShoppingListProductsUpdate
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList

interface ShoppingListProductsUpdateDAO {
    fun findByShoppingListOrNull(list: ShoppingList): RecentShoppingListProductsUpdate?
    fun save(previousUpdate: RecentShoppingListProductsUpdate)
}