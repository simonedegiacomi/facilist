package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.stereotype.Component

@Component
class WebSocketDeliveryMethod:NotificationDeliveryMethod {

    override fun canDeliverToUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deliverToUser(user: User, notification: Notification) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}