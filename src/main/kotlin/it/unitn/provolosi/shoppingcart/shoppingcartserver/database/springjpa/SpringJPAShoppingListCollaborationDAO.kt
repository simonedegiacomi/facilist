package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserAlreadyCollaboratesWithShoppingListException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCollaborationDAO : JpaRepository<ShoppingListCollaboration, Long>

@Component
class SpringJPAShoppingListCollaborationDAO(
        private val springRepository: InternalSpringJPAShoppingListCollaborationDAO
) : ShoppingListCollaborationDAO {

    override fun save(collaboration: ShoppingListCollaboration) = try {
        springRepository.save(collaboration)

    } catch (ex: DataIntegrityViolationException) {
        if (ex.toString().contains(ShoppingListCollaboration.SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT, true)) {
            throw UserAlreadyCollaboratesWithShoppingListException()
        } else {
            throw ex
        }
    }


    override fun findById(id: Long) = springRepository.findById(
        id).orElseThrow { ShoppingListCollaborationNotFoundException() }!!

    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException) {
        throw ShoppingListCollaborationNotFoundException()
    }
}