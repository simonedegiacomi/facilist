package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class ProductCategoryNotFoundException : Exception()
class ProductCategoryWithSameNameAlreadyExistsException : Exception()

interface ProductCategoryDAO {

    fun save(category: ProductCategory): ProductCategory

    fun existsWithName(name: String): Boolean

    fun findAllByOrderByNameAsc(): List<ProductCategory>

    fun findAllByOrderByNameAsc(page: Pageable): Page<ProductCategory>

    fun findById(id: Long): ProductCategory

    fun deleteById(id: Long)

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<ProductCategory>
}

