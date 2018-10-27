package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface IRealtimeUpdatesService {

    fun userNewShoppingList(user: User, list: ShoppingList)

    fun shoppingListInfoEdited(list: ShoppingList)

    fun shoppingListDeleted(list: ShoppingList)
}