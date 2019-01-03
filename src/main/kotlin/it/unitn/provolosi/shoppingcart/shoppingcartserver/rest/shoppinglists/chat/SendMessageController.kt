package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import ok
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/chat/messages")
class SendMessageController(
        private val chatMessageDAO: ChatMessageDAO,
        private val syncService: WebSocketSyncService,
        private val shoppingListNotifications: ShoppingListNotifications
) {

    companion object {
        /**
         * Threashold used to determine if a new message should cause a notification
         */
        const val NEW_MESSAGE_THRESHOLD = 5 * 60 * 1000
    }

    /**
     * Handles the request to send a message.
     * When a new message is sent, if the messages is the first or the first after a period of time, a notification is
     * sent to the collaborators if the list
     */
    @PostMapping
    fun sendMessage(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList,
            @RequestBody @Valid @NotEmpty message: String
    ): ResponseEntity<ChatMessage> {
        val chatMessage = chatMessageDAO.save(ChatMessage(
            user = user,
            shoppingList = list,
            message = message
        ))

        syncService.newMessageInShoppingList(chatMessage)

        sendNotificationToCollaboratorsIfNeeded(chatMessage)

        return ok(chatMessage)
    }

    /**
     * When a new message is sent in a list a notification in sent to the other collaborators if needed
     */
    private fun sendNotificationToCollaboratorsIfNeeded(message: ChatMessage) {
        if (shouldSendNotification(message)) {
            shoppingListNotifications.notifyCollaboratorsNewMessage(message)
        }
    }

    /**
     * Checks if collaborators should be notified about the new message
     */
    private fun shouldSendNotification(sentMessage: ChatMessage): Boolean {
        val recentMessages = loadLastTwoMessagesSentInList(sentMessage.shoppingList)

        if (recentMessages.count() == 1) {
            return true // First message in the list
        }

        // Take the message sent before the one just sent
        val olderMessage = recentMessages.content[1]
        val newMessageSentAfter = sentMessage.sentAt.time - olderMessage.sentAt.time

        // Verify if the previous message was sent too much time ago
        return newMessageSentAfter > NEW_MESSAGE_THRESHOLD
    }

    private fun loadLastTwoMessagesSentInList(list: ShoppingList) = chatMessageDAO.findByShoppingListOrderBySentAtDesc(
        list, PageRequest.of(0, 2)
    )
}