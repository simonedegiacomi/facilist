package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class ProductNotFoundException : Exception()

interface ProductDAO {

    fun findAllByOrderByNameAsc(page: Pageable): Page<Product>

    fun save(product: Product): Product

    fun deleteById(id: Long)

}