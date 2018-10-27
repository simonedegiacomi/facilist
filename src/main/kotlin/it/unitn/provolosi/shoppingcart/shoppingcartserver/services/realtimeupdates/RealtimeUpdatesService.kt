package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates.SyncEvent.Companion.EVENT_CREATED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates.SyncEvent.Companion.EVENT_DELETED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.realtimeupdates.SyncEvent.Companion.EVENT_MODIFIED
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class RealtimeUpdatesService(
        private val stomp: SimpMessagingTemplate
): IRealtimeUpdatesService {


    override fun userNewShoppingList(user: User, list: ShoppingList) = stomp.convertAndSendToUser(
        user.email,
        "/queue/shoppingLists",
        SyncEvent(EVENT_CREATED, list.toPreview())
    )

    override fun shoppingListInfoEdited(list: ShoppingList) = stomp.convertAndSend(
        "/topic/shoppingLists/${list.id}",
        SyncEvent(EVENT_MODIFIED, list.toPreview())
    )

    override fun shoppingListDeleted(list: ShoppingList) = stomp.convertAndSend(
        "/topic/shoppingLists/${list.id}",
        SyncEvent<ShoppingList>(EVENT_DELETED)
    )


}


data class SyncEvent<T> (
        val event: String,
        val model: T? = null
) {
    companion object {
        const val EVENT_CREATED     = "created"
        const val EVENT_MODIFIED    = "modified"
        const val EVENT_DELETED     = "deleted"
    }
}