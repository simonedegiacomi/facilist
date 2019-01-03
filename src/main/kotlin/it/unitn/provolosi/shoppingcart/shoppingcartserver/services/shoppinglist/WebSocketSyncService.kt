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

    private fun sendSyncQueueEvent(email: String, destination: String, event: String, model: Any) = stomp.convertAndSendToUser(
        email,
        destination,
        SyncEvent(event, model)
    )

    private fun sendSyncTopicEvent(destination: String, event: String, model: Any? = null) = stomp.convertAndSend(
        destination,
        SyncEvent(event, model)
    )


    override fun userNewShoppingList(user: User, list: ShoppingList) = sendSyncQueueEvent(
        user.email,
        "/queue/shoppingLists",
        EVENT_CREATED,
        list.toPreview()
    )

    override fun shoppingListInfoEdited(list: ShoppingList) = sendSyncTopicEvent(
        "/topic/shoppingLists/${list.id}",
        EVENT_MODIFIED,
        list.toPreview()
    )

    override fun shoppingListDeleted(list: ShoppingList) = sendSyncTopicEvent(
        "/topic/shoppingLists/${list.id}",
        EVENT_DELETED

    )

    override fun newShoppingListProduct(relation: ShoppingListProduct) = sendSyncTopicEvent(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        EVENT_CREATED,
        relation
    )

    override fun productInShoppingListEdited(relation: ShoppingListProduct) = sendSyncTopicEvent(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        EVENT_MODIFIED,
        relation
    )


    override fun productInShoppingListDeleted(relation: ShoppingListProduct) = sendSyncTopicEvent(
        "/topic/shoppingLists/${relation.shoppingList.id}/products",
        EVENT_DELETED,
        relation
    )

    override fun newMessageInShoppingList(message: ChatMessage) = sendSyncTopicEvent(
        "/topic/shoppingLists/${message.shoppingList.id}/chat/messages",
        EVENT_CREATED,
        message
    )


    override fun newCollaborator(collaboration: ShoppingListCollaboration) = sendSyncTopicEvent(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        EVENT_CREATED,
        collaboration
    )

    override fun collaborationEdited(collaboration: ShoppingListCollaboration) = sendSyncTopicEvent(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        EVENT_MODIFIED,
        collaboration
    )

    override fun collaborationDeleted(collaboration: ShoppingListCollaboration) = sendSyncTopicEvent(
        "/topic/shoppingLists/${collaboration.shoppingList.id}/collaborations",
        EVENT_DELETED,
        collaboration
    )

    override fun newInvite(list: ShoppingList, invite: InviteToJoin) = sendSyncTopicEvent(
        "/topic/shoppingLists/${list.id}/invites",
        EVENT_CREATED,
        invite
    )

    override fun inviteDeleted(list: ShoppingList, inviteId: Long) = sendSyncTopicEvent(
        "/topic/shoppingLists/${list.id}/invites",
        EVENT_DELETED,
        mapOf("id" to inviteId)
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
