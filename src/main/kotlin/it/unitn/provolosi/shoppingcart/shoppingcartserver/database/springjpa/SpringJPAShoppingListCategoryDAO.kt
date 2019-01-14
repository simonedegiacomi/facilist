package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryWithSameNameAlreadyExistsException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory.Companion.SHOPPING_LIST_CATEGORY_UNIQUE_NAME_CONSTRAINT
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCategoryDAO : JpaRepository<ShoppingListCategory, Long> {

    fun findAllByOrderByNameAsc(): List<ShoppingListCategory>
}

@Component
class SpringJPAShoppingListCategoryDAO(
        private val springRepository: InternalSpringJPAShoppingListCategoryDAO
) : ShoppingListCategoryDAO {

    override fun save(category: ShoppingListCategory) = runAndMapConstraintFailureTo(
        SHOPPING_LIST_CATEGORY_UNIQUE_NAME_CONSTRAINT,
        { ShoppingListCategoryWithSameNameAlreadyExistsException() },
        { springRepository.save(category) }
    )

    override fun findById(id: Long): ShoppingListCategory = springRepository.findById(
        id).orElseThrow { ShoppingListCategoryNotFoundException() }

    override fun findAllByOrderByNameAsc() = springRepository.findAllByOrderByNameAsc()

    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException){
        throw ShoppingListCategoryNotFoundException()
    }
}