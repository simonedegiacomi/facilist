package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory

class ShoppingListCategoryNotFoundException : Exception()
class ShoppingListCategoryWithSameNameAlreadyExistsException : Exception()

interface ShoppingListCategoryDAO {

    fun save(category: ShoppingListCategory): ShoppingListCategory

    fun findAllByOrderByNameAsc(): List<ShoppingListCategory>

    fun findById(id: Long): ShoppingListCategory

    fun deleteById(id: Long)
}