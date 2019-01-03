package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListPreview
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

class ShoppingListNotFoundException : Exception()



interface ShoppingListDAO {

    fun save(shoppingList: ShoppingList): ShoppingList

    /**
     * Returns a flatten version of the shopping lists of the user
     */
    fun getShoppingListPreviewsByUser(user: User): List<ShoppingListPreview>

    fun findById(id: Long): ShoppingList

    fun delete(list: ShoppingList)

}