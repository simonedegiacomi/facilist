package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_CREATED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_DELETED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_MODIFIED
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class SyncShoppingListService(
        private val stomp: SimpMessagingTemplate
) : ISyncShoppingListService {


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


    override fun newShoppingListProduct(relation: ShoppingListProduct) = stomp.convertAndSend(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        SyncEvent(EVENT_CREATED, relation)
    )

    override fun productInShoppingListEdited(relation: ShoppingListProduct) = stomp.convertAndSend(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        SyncEvent(EVENT_MODIFIED, relation)
    )


}


data class SyncEvent<T>(
        val event: String,
        val model: T? = null
) {
    companion object {
        const val EVENT_CREATED = "created"
        const val EVENT_MODIFIED = "modified"
        const val EVENT_DELETED = "deleted"
    }
}