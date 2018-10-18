package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListProductDAO : JpaRepository<ShoppingListProduct, Long>

@Component
class SpringJPAShoppingListProductDAO(
        private val springRepository: InternalSpringJPAShoppingListProductDAO
) : ShoppingListProductDAO {

    override fun save(product: ShoppingListProduct) = springRepository.save(product)

}