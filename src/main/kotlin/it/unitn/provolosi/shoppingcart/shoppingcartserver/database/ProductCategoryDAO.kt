package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class ProductCategoryNotFoundException : Exception()
class ProductCategoryWithSameNameAlreadyExistsException : Exception()

interface ProductCategoryDAO {

    fun save(category: ProductCategory): ProductCategory

    /**
     * Checks if a ProductCategory with the specified name exists
     */
    fun existsWithName(name: String): Boolean

    fun findAllByOrderByNameAsc(): List<ProductCategory>

    /**
     * Return a list of Pair object. The first item in the Pair is the ProductCategory and the second is the number of
     * Products that belongs to that category.
     */
    fun findAllWithProductsCountByOrderByNameAsc(): List<Pair<ProductCategory, Long>>

    fun findAllByOrderByNameAsc(page: Pageable): Page<ProductCategory>

    fun findById(id: Long): ProductCategory

    fun deleteById(id: Long)

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<ProductCategory>
}

