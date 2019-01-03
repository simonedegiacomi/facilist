package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserAlreadyCollaboratesWithShoppingListException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration.Companion.SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCollaborationDAO : JpaRepository<ShoppingListCollaboration, Long>

@Component
class SpringJPAShoppingListCollaborationDAO(
        private val springRepository: InternalSpringJPAShoppingListCollaborationDAO
) : ShoppingListCollaborationDAO {

    override fun save(collaboration: ShoppingListCollaboration) = runAndMapConstraintFailureTo(
        SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT,
        { UserAlreadyCollaboratesWithShoppingListException() },
        { springRepository.save(collaboration) }
    )


    override fun findById(id: Long) = springRepository.findById(
        id).orElseThrow { ShoppingListCollaborationNotFoundException() }!!

    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException) {
        throw ShoppingListCollaborationNotFoundException()
    }
}