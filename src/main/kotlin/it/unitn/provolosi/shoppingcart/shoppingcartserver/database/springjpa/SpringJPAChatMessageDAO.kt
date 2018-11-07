package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

interface InternalSpringJPAChatMessageDAO : JpaRepository<ChatMessage, Long> {

    fun findByShoppingListOrderBySentAtDesc(list: ShoppingList, pageable: Pageable): Page<ChatMessage>

    @Query(
        "FROM ChatMessage m " +
                "WHERE m.sentAt < (SELECT m2.sentAt FROM ChatMessage m2 WHERE m2.id = :lastMessageId) AND " +
                "m.shoppingList = :list " +
                "ORDER BY m.sentAt DESC"
    )
    fun findByShoppingListOlderThanMessageOrderBySentAtDesc(
            list: ShoppingList,
            lastMessageId: Long,
            pageable: Pageable
    ): Page<ChatMessage>

}

@Component
class SpringJPAChatMessageDAO(
        private val springRepository: InternalSpringJPAChatMessageDAO
) : ChatMessageDAO {

    override fun save(chatMessage: ChatMessage) = springRepository.save(chatMessage)

    override fun findByShoppingListOrderBySentAtDesc(list: ShoppingList, pageable: Pageable) =
            springRepository.findByShoppingListOrderBySentAtDesc(list, pageable)

    override fun findByShoppingListOlderThanMessageOrderBySentAtDesc(
            list: ShoppingList,
            lastMessageId: Long,
            pageable: Pageable
    ) = springRepository.findByShoppingListOlderThanMessageOrderBySentAtDesc(list, lastMessageId, pageable)
}