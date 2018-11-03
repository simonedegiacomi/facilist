package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
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
        private val chatMessageDAO: ChatMessageDAO
) {

    @PostMapping
    fun sendMessage(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList,
            @RequestBody @Valid @NotEmpty message: String
    ): ResponseEntity<ChatMessage> {
        val message         = chatMessageDAO.save(ChatMessage(
            user            = user,
            shoppingList    = list,
            message         = message
        ))

        // TODO: Sync and send notification

        ResponseEntity.ok(message)
    }
}