package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductsUpdateDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProductUpdatesGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*

interface InternalSpringJPAShoppingListProductsUpdateDAO : JpaRepository<ShoppingListProductUpdatesGroup, Long> {

    fun findByShoppingList (list: ShoppingList): ShoppingListProductUpdatesGroup?

    fun findByLastEditAtLessThan(lessThan: Date): List<ShoppingListProductUpdatesGroup>
}

@Component
class SpringJPAShoppingListProductsUpdateDAO(
        private val springRepository: InternalSpringJPAShoppingListProductsUpdateDAO
) : ShoppingListProductsUpdateDAO {

    override fun findByShoppingListOrNull(list: ShoppingList) = springRepository.findByShoppingList(list)

    override fun save(update: ShoppingListProductUpdatesGroup) = springRepository.save(update)

    override fun findByLastEditAtLessThan(lessThan: Date) = springRepository.findByLastEditAtLessThan(lessThan)

    override fun delete(update: ShoppingListProductUpdatesGroup) {
        update.shoppingList.productUpdatesGroup = null
        update.updatedProducts.forEach { p -> p.shoppingListProductUpdatesGroup = null }
        springRepository.delete(update)
    }
}