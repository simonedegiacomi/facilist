package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProductUpdatesGroup
import java.util.*

interface ShoppingListProductsUpdateDAO {
    fun findByShoppingListOrNull(list: ShoppingList): ShoppingListProductUpdatesGroup?
    fun save(update: ShoppingListProductUpdatesGroup): ShoppingListProductUpdatesGroup
    fun findByLastEditAtLessThan(lessThan: Date): List<ShoppingListProductUpdatesGroup>
    fun delete(update: ShoppingListProductUpdatesGroup)
}