package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.pushnotification.IPushNotification
import org.springframework.stereotype.Component

@Component
class PushNotificationDeliveryMethod: NotificationDeliveryMethod {

    override fun canDeliverToUser(user: User) = user.pushSubscriptions.size > 0

    override fun deliverToUser(user: User, notification: Notification) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}