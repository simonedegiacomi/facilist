package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Component

@Component
class WebSocketDeliveryMethod(
        private val stomp: SimpMessagingTemplate,
        private val userRegistry: SimpUserRegistry
): NotificationDeliveryMethod {


    override fun canDeliver(notification: Notification) = userRegistry.getUser(notification.target.email) != null

    override fun deliver(notification: Notification) {
        stomp.convertAndSendToUser(
            notification.target.email,
            "/queue/notifications",
            SyncEvent(SyncEvent.EVENT_CREATED, notification)
        )
    }
}
