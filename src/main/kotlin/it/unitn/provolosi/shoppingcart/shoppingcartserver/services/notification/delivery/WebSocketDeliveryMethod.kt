package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Component

/**
 * DeliveryMethod based on websockets
 */
@Component
class WebSocketDeliveryMethod(

        /**
         * Onejct to send STOMP messages using websocket
         */
        private val stomp: SimpMessagingTemplate,

        /**
         * Object that contains a map of currently connected users
         */
        private val userRegistry: SimpUserRegistry
): NotificationDeliveryMethod {

    /**
     * Checks if the notification can be sent using websocket right now. If the user isn't connected this method
     * will return false
     */
    override fun canDeliver(notification: Notification) = userRegistry.getUser(notification.target.email) != null

    /**
     * Sends the notification using websocket. If the notification can't be sent no exception will be thrown, it's the
     * called responsibility to call the canDelivery method before.
     */
    override fun deliver(notification: Notification) {
        stomp.convertAndSendToUser(
            notification.target.email,
            "/queue/notifications",
            SyncEvent(SyncEvent.EVENT_CREATED, notification)
        )
    }
}
