package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Update
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface IRealtimeUpdatesService {
    fun send(updateNewCollaborator: Update)

    fun userNewShoppingList(user: User, list: ShoppingList)
}