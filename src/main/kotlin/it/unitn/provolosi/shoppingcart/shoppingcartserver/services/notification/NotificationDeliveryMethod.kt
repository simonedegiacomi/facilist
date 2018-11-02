package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification

class CantDeliverToUserException : Exception()

interface NotificationDeliveryMethod {

    fun canDeliver(notification: Notification): Boolean

    fun deliver(notification: Notification)

}