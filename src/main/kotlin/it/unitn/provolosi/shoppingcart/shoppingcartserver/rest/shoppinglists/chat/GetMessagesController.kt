package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/chat/messages")
class GetMessagesController(
        private val chatMessageDAO: ChatMessageDAO
) {


    @GetMapping
    fun sendMessage(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<ChatMessage>> = ResponseEntity.ok(
        chatMessageDAO.findByShoppingListOrderBySentAtDesc(list, PageRequest.of(page, size))
    )
}