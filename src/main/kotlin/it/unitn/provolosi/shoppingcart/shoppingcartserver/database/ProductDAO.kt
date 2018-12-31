package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class ProductNotFoundException : Exception()

interface ProductDAO {

    fun findAllByOrderByNameAsc(page: Pageable): Page<Product>

    fun save(product: Product): Product

    fun deleteById(id: Long)

    fun findById(id: Long): Product

    fun findByNameContainingIgnoreCaseAndCreatedByAdminOrderByName(name: String, pageable: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndCategoryIdAndCreatedByAdminOrderByName
                (name: String, categoryId: Long, pageable: Pageable): Page<Product>

    fun findByCategoryIdAndCreatedByAdminOrderByName(categoryId: Long, pageable: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrderByName
                (name: String, categoryId: Long, pageable: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrUserOrderByName
                (name: String, categoryId: Long, user: User, pageable: Pageable): Page<Product>

}