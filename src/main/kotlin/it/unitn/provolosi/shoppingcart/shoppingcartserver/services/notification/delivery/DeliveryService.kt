package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.Notification
import org.springframework.stereotype.Component

@Component
class DeliveryService: IDeliveryService {
    override fun sendNotification(notification: Notification) {
        println(notification)
    }
}