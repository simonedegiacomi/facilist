package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.Notification

interface IDeliveryService {

    fun sendNotifications (notifications: List<Notification>) = notifications.forEach { n -> sendNotification(n) }

    fun sendNotification (notification: Notification)

}
