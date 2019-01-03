package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification

/**
 * Interface of a Service that can send notifications
 */
interface NotificationDeliveryMethod {

    /**
     * Checks if the delivery method can send the specified notification right now
     */
    fun canDeliver(notification: Notification): Boolean

    /**
     * Sends the specified notification
     */
    fun deliver(notification: Notification)

}