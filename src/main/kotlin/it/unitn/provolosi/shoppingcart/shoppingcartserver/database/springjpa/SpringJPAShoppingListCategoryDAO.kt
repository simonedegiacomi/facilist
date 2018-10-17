package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryWithSameNameAlreadyExistsException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCategoryDAO : JpaRepository<ShoppingListCategory, Long> {

    fun findAllByOrderByNameAsc(): List<ShoppingListCategory>
}

@Component
class SpringJPAShoppingListCategoryDAO(
        private val springRepository: InternalSpringJPAShoppingListCategoryDAO
) : ShoppingListCategoryDAO {

    override fun save(category: ShoppingListCategory) = try {
        springRepository.save(category)

    } catch (ex: DataIntegrityViolationException) {
        if (ex.toString().contains(ShoppingListCategory.SHOPPING_LIST_CATEGORY_UNIQUE_NAME_CONSTRAINT, true)) {
            throw ShoppingListCategoryWithSameNameAlreadyExistsException()
        } else {
            throw RuntimeException("database error")
        }
    }

    override fun findById(id: Long): ShoppingListCategory = springRepository.findById(
        id).orElseThrow { ShoppingListCategoryNotFoundException() }

    override fun findAllByOrderByNameAsc() = springRepository.findAllByOrderByNameAsc()
}