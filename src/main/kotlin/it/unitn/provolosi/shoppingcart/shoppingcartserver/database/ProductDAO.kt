package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

class ProductNotFoundException : Exception()

interface ProductDAO {

    fun findAllByOrderByNameAsc(page: Pageable): Page<Product>

    fun save(product: Product): Product

    fun deleteById(id: Long)

    fun findById(id: Long): Product

    fun findByNameContainingIgnoreCaseOrderByName(name: String, pageable: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndCategoryIdOrderByName(name: String, categoryId: Long, pageable: Pageable): Page<Product>

    fun findByCategoryIdOrderByName(categoryId: Long, pageable: Pageable): Page<Product>

}