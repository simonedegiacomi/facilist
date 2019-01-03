package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProductUpdatesGroup
import java.util.*

interface ShoppingListProductsUpdateDAO {

    fun findByShoppingListOrNull(list: ShoppingList): ShoppingListProductUpdatesGroup?

    fun save(update: ShoppingListProductUpdatesGroup): ShoppingListProductUpdatesGroup

    /**
     * Returns a list of groups of notifications that contains edits happened before the specified date
     */
    fun findByLastEditAtLessThan(lessThan: Date): List<ShoppingListProductUpdatesGroup>

    fun delete(update: ShoppingListProductUpdatesGroup)

}