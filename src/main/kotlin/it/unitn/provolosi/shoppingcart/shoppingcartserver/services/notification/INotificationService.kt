package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface INotificationService {

    fun saveAndSendCollaboratorNotification(collaboration: ShoppingListCollaboration, inviter: User, action: String)

}

