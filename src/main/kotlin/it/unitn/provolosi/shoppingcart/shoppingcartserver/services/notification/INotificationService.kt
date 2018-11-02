package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface INotificationService {

    fun saveAndSendCollaboratorNotification(collaboration: ShoppingListCollaboration, inviter: User, action: String)

    //fun saveAndSendProductNotification(product: ShoppingListProduct, source: User, action: String)

    fun saveAndSendShoppingListInfoNotification(list: ShoppingList, source: User, action: String)

}

