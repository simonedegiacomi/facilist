package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

class ShoppingListNotFoundException : Exception()

data class ShoppingListPreview(
        val id: Long,
        val name: String,
        val description: String?,
        val icon: String,

        val itemsCount: Int,
        val itemsToBuyCount: Int,

        val isShared: Boolean
)

interface ShoppingListDAO {

    fun save(shoppingList: ShoppingList): ShoppingList

    fun getShoppingListPreviewsByUser(user: User): List<ShoppingListPreview>

    fun findById(id: Long): ShoppingList

}