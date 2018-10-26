package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.pushnotification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import org.springframework.stereotype.Component

@Component
class PushNotificationService:IPushNotification {
    override fun send(notification: Notification) {
        println("Invio la notifica")
    }

    override fun sendBuffered(notification: Notification) {
        println("Invio la notifica bufferizzata")
    }
}