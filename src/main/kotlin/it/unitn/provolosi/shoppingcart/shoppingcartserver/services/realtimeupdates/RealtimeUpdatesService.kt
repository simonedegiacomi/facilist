package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Update
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class RealtimeUpdatesService(
        private val stomp: SimpMessagingTemplate
): IRealtimeUpdatesService {

    override fun send(update: Update) {
        stomp.convertAndSend("/shoppingLists/${update.notification.shoppingList.id}", update)
    }

    override fun userNewShoppingList(user: User, list: ShoppingList) =
            stomp.convertAndSendToUser(user.email, "/queue/shoppingLists", list)
}
