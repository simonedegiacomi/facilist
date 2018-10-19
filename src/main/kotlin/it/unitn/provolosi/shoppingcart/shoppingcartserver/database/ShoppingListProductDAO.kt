package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct

interface ShoppingListProductDAO {
    fun save(product: ShoppingListProduct): ShoppingListProduct
    fun deleteAll(toDelete: List<ShoppingListProduct>)
}