package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification

class CantDeliverToUserException : Exception()

interface NotificationDeliveryMethod {

    fun canDeliver(notification: Notification): Boolean

    fun deliver(notification: Notification)

}