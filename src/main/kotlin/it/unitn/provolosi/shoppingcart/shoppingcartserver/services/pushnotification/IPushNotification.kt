package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.pushnotification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification

interface IPushNotification {
    fun send(notification: Notification)
    fun sendBuffered(notification: Notification)
}