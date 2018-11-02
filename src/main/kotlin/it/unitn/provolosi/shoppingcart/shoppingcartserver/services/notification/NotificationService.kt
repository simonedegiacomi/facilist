package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListCollaborationNotification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListNotification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.IDeliveryService
import org.springframework.stereotype.Component

@Component
class NotificationService (
        private val notificationDAO: NotificationDAO,
        private val deliveryService: IDeliveryService
): INotificationService {

    override fun saveAndSendCollaboratorNotification(
            collaboration: ShoppingListCollaboration,
            inviter: User,
            action: String
    ) {
        val list = collaboration.shoppingList
        val notifications = list.ownerAndCollaborators()
                .asSequence() // TODO: Find out why IntelliJ suggest this call
                .filter { u -> u != inviter }
                .map { u ->
                    ShoppingListCollaborationNotification(
                        shoppingList    = list,
                        source          = inviter,
                        target          = u,
                        action          = action,
                        collaboration   = collaboration
                    )
                }
                .toList()

        notificationDAO.saveAll(notifications)

        deliveryService.sendNotifications(notifications)
    }

    override fun saveAndSendShoppingListInfoNotification(
            list: ShoppingList,
            source: User,
            action: String
    ) {
        val notifications = list.ownerAndCollaborators()
                .asSequence() // TODO: Find out why IntelliJ suggest this call
                .filter { u -> u != source }
                .map { u ->
                    ShoppingListNotification(
                        shoppingList    = list,
                        source          = source,
                        target          = u,
                        action          = action
                    )
                }
                .toList()

        notificationDAO.saveAll(notifications)

        deliveryService.sendNotifications(notifications)
    }
}