package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/shoppingLists/:id/chat/messages")
class SendMessageController(
        private val shoppingListDAO: ShoppingListDAO,
        private val chatMessageDAO: ChatMessageDAO
) {

    @PostMapping
    fun sendMessage(
            @AppUser user: User,
            @PathVariable("id") listId: Long,
            @RequestBody @Valid @NotEmpty message: String
    ): ResponseEntity<ChatMessage> = try {
        val list = shoppingListDAO.findById(listId)
        if (list.isUserOwnerOrCollaborator(user)) {

            val message = chatMessageDAO.save(ChatMessage(
                user            = user,
                shoppingList    = list,
                message         = message
            ))

            // TODO: Sync and send notification

            ResponseEntity.ok(message)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }
}