package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import org.springframework.stereotype.Component

@Component
class PushNotificationDeliveryMethod: NotificationDeliveryMethod {

    override fun canDeliver(notification: Notification) = notification.target.pushSubscriptions.size > 0

    override fun deliver(notification: Notification) {
        TODO("not implemented")
    }
}