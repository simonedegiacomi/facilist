package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductAlreadyInShoppingListException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListProductDAO : JpaRepository<ShoppingListProduct, Long>

@Component
class SpringJPAShoppingListProductDAO(
        private val springRepository: InternalSpringJPAShoppingListProductDAO
) : ShoppingListProductDAO {

    override fun save(product: ShoppingListProduct) = try {
        springRepository.save(product)
    } catch (ex: DataIntegrityViolationException) {
        if (ex.toString().contains(ShoppingListProduct.SHOPPING_LIST_PRODUCT_UNIQUE_CONSTRAINT, true)) {
            throw ProductAlreadyInShoppingListException()
        } else {
            throw ex
        }
    }


    override fun deleteAll(toDelete: List<ShoppingListProduct>) = springRepository.deleteAll(toDelete)

    override fun findById(id: Long): ShoppingListProduct = springRepository.findById(id)
            .orElseThrow { ShoppingListProductNotFoundException() }

    override fun delete(relation: ShoppingListProduct) = springRepository.delete(relation)
}
