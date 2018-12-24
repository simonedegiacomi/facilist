package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
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
        private val syncService: SyncService,
        private val notificationService: NotificationService
) {

    companion object {
        const val NEW_MESSAGE_THRESHOLD = 5 * 60 * 1000
    }

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

        return ResponseEntity.ok(chatMessage)
    }

    /**
     * When a new message is sent in a list a notification in sent to the other collaborators
     */
    private fun sendNotificationToCollaboratorsIfNeeded(message: ChatMessage) {
        if (shouldSendNotification(message)) {
            sendNotificationOfNewMessage(message)
        }
    }


    private fun shouldSendNotification(sentMessage: ChatMessage): Boolean {
        val recentMessages = chatMessageDAO.findByShoppingListOrderBySentAtDesc(
            sentMessage.shoppingList, PageRequest.of(0, 2)
        )

        if (recentMessages.count() == 1) {
            return true // First message in the list
        }

        val olderMessage = recentMessages.content[1]
        val newMessageSentAfter = sentMessage.sentAt.time - olderMessage.sentAt.time
        return newMessageSentAfter > NEW_MESSAGE_THRESHOLD
    }

    /**
     * Send a notification to the collaborators except the user who sent the message
     */
    private fun sendNotificationOfNewMessage(message: ChatMessage) =
            notificationService.saveAndSend(buildNotification(message))

    private fun buildNotification(message: ChatMessage) = getCollaboratorsExpect(message.shoppingList, message.user)
            .map { user -> Notification(
                target  = user,
                message = buildNotificationMessage(message),
                icon    = message.shoppingList.icon,
                url     = "/user/shoppingLists/${message.shoppingList.id}"
            ) }

    private fun buildNotificationMessage(message: ChatMessage) =
            "${message.user.firstName} ha inviato un nuovo messaggio nella lista \"${message.shoppingList.name}\""

    private fun getCollaboratorsExpect(list: ShoppingList, user: User) = list.ownerAndCollaborators()
            .filter { it != user }
}