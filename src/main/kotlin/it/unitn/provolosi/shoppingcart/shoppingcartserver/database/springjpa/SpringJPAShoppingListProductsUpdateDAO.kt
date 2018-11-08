package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductsUpdateDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.RecentShoppingListProductsUpdate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListProductsUpdateDAO : JpaRepository<RecentShoppingListProductsUpdate, Long>

@Component
class SpringJPAShoppingListProductsUpdateDAO(
        private val springRepository: InternalSpringJPAShoppingListProductsUpdateDAO
) : ShoppingListProductsUpdateDAO {


}