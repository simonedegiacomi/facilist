package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface ISyncShoppingListService {

    fun userNewShoppingList(user: User, list: ShoppingList)

    fun shoppingListInfoEdited(list: ShoppingList)

    fun shoppingListDeleted(list: ShoppingList)
}