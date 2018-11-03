package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.chat

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/shoppingLists/:id/chat/messages")
class GetMessagesController (
        private val shoppingListDAO: ShoppingListDAO,
        private val chatMessageDAO: ChatMessageDAO
) {


    @GetMapping
    fun sendMessage(
            @AppUser user: User,
            @PathVariable("id") listId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<ChatMessage>> = try {
        val list = shoppingListDAO.findById(listId)
        if (list.isUserOwnerOrCollaborator(user)) {

            ResponseEntity.ok(
                chatMessageDAO.findByShoppingListOrderBySentAtDesc(list, PageRequest.of(page, size))
            )

        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }
}