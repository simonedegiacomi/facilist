package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAChatMessageDAO : JpaRepository<ChatMessage, Long> {

    fun findByShoppingListOrderBySentAtDesc(list: ShoppingList, pageable: Pageable): Page<ChatMessage>


}

@Component
class SpringJPAChatMessageDAO(
        private val springRepository: InternalSpringJPAChatMessageDAO
) : ChatMessageDAO {

    override fun save(chatMessage: ChatMessage) = springRepository.save(chatMessage)

    override fun findByShoppingListOrderBySentAtDesc(list: ShoppingList, pageable: Pageable) =
            springRepository.findByShoppingListOrderBySentAtDesc(list, pageable)
}