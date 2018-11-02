package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChatMessageDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAChatMessageDAO : JpaRepository<ChatMessage, Long> {}

@Component
class SpringJPAChatMessageDAO(
        private val springRepository: InternalSpringJPAChatMessageDAO
) : ChatMessageDAO {

}