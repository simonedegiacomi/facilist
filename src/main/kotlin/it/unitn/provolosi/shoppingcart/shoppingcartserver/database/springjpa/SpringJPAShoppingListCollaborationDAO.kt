package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCollaborationDAO: JpaRepository<ShoppingListCollaboration, Long>

@Component
class SpringJPAShoppingListCollaborationDAO (
        private val springRepository: InternalSpringJPAShoppingListCollaborationDAO
): ShoppingListCollaborationDAO {

    override fun save(collaboration: ShoppingListCollaboration) = springRepository.save(collaboration)

}