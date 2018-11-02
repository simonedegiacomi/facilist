package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListCollaborationNotification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListNotification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.IDeliveryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * This service persist the notifications and send them using:
 * - WebSocket if at least one client of the destination user is connected
 * - Push Notifications otherwise
 */
@Component
class NotificationService(
        private val notificationDAO: NotificationDAO,

        webSocket: WebSocketDeliveryMethod,
        pushNotification: PushNotificationDeliveryMethod
) {

    val deliveryMethods = listOf(webSocket, pushNotification)

    fun saveAndSend (notification: Notification) {
        notificationDAO.save(notification)

        send(notification)
    }

    fun saveAndSend(notifications: List<Notification>) {
        notificationDAO.saveAll(notifications)

        notifications.forEach { n -> send(n) }
    }

    private fun send(notification: Notification) = deliveryMethods
            .firstOrNull { method -> method.canDeliver(notification) }
            ?.deliver(notification)

}