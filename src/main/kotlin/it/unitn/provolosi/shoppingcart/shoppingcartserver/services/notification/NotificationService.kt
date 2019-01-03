package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.PushNotificationDeliveryMethod
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.WebSocketDeliveryMethod
import org.springframework.stereotype.Component

/**
 * This service persist the notifications and sends them using:
 * - WebSocket: if at least one client of the destination user is connected;
 * - Push Notifications otherwise
 */
@Component
class NotificationService(
        private val notificationDAO: NotificationDAO,

        /**
         * Helper to send notification using websockets
         */
        webSocket: WebSocketDeliveryMethod,

        /**
         * Helper to send notification using Push API
         */
        pushNotification: PushNotificationDeliveryMethod
) {

    val deliveryMethods = listOf(webSocket, pushNotification)

    /**
     * Persists a notification into the database and sends it to the target user
     */
    fun saveAndSend (notification: Notification) {
        notificationDAO.save(notification)

        send(notification)
    }

    /**
     * Persists a list of notifications and sends them to the target users
     */
    fun saveAndSend(notifications: List<Notification>) {
        notificationDAO.saveAll(notifications)

        notifications.forEach { n -> send(n) }
    }

    /**
     * Send a notification using the first delivery method available (first tries with websocket, then fallback to Push API)
     */
    private fun send(notification: Notification) = deliveryMethods
            .firstOrNull { method -> method.canDeliver(notification) }
            ?.deliver(notification)

}