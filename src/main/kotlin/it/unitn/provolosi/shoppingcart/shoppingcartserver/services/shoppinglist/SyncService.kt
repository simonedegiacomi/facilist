package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_CREATED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_DELETED
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncEvent.Companion.EVENT_MODIFIED
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class SyncService(
        private val stomp: SimpMessagingTemplate
) : ISyncService {


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


    override fun productInShoppingListDeleted(relation: ShoppingListProduct) = stomp.convertAndSend(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        SyncEvent(EVENT_DELETED, relation)
    )

    override fun newMessageInShoppingList(message: ChatMessage) = stomp.convertAndSend(
        "/topic/shoppingLists/${message.shoppingList.id}/chat/messages",
        SyncEvent(EVENT_CREATED, message)
    )


    override fun newCollaborator(collaboration: ShoppingListCollaboration) = stomp.convertAndSend(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        SyncEvent(EVENT_CREATED, collaboration)
    )

    override fun collaborationEdited(collaboration: ShoppingListCollaboration) = stomp.convertAndSend(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        SyncEvent(EVENT_MODIFIED, collaboration)
    )

    override fun collaborationDeleted(collaboration: ShoppingListCollaboration) = stomp.convertAndSend(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        SyncEvent(EVENT_DELETED, collaboration)
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
