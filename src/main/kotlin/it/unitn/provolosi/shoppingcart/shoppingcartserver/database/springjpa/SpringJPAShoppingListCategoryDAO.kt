package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListCategoryDAO : JpaRepository<ShoppingListCategory, Long> {

}

@Component
class SpringJPAShoppingListCategoryDAO(
        private val springRepository: InternalSpringJPAShoppingListCategoryDAO
) : ShoppingListCategoryDAO {

    override fun save(category: ShoppingListCategory) = springRepository.save(category)
}