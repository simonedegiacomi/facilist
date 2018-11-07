package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct

class ShoppingListProductNotFoundException : Exception()
class ProductAlreadyInShoppingListException: Exception()

interface ShoppingListProductDAO {

    fun save(product: ShoppingListProduct): ShoppingListProduct

    fun deleteAll(toDelete: List<ShoppingListProduct>)

    fun findById(id: Long): ShoppingListProduct

    fun delete(relation: ShoppingListProduct)
}