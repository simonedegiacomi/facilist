package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductCategoryDAO {

    fun save(category: ProductCategory)

    fun existsWithName(name: String):Boolean

    fun findAllByOrderByNameAsc(page: Pageable): Page<ProductCategory>
}
