package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory

interface ShoppingListCategoryDAO {
    fun save(category: ShoppingListCategory): ShoppingListCategory
}