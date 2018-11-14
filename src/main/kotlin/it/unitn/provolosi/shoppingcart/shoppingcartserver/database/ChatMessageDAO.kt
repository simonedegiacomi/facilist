package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatMessageDAO {

    fun save(chatMessage: ChatMessage): ChatMessage

    fun findByShoppingListOrderBySentAtDesc(list: ShoppingList, pageable: Pageable): Page<ChatMessage>
    fun findByShoppingListOlderThanMessageOrderBySentAtDesc(list: ShoppingList, lastMessageId: Long, pageable: Pageable): Page<ChatMessage>

}