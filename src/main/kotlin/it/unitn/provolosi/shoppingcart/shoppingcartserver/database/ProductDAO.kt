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

    /**
     * Return a list of products filtered by the specified name. The returned products can be added to lists of the
     * specified id and the products are created by admins
     */
    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrderByName
                (name: String, categoryId: Long, pageable: Pageable): Page<Product>

    /**
     * Return a list of products filtered by the specified name. The returned products can be added to lists of the
     * specified id and the products are created by admins or the specified user
     */
    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrUserOrderByName
                (name: String, categoryId: Long, user: User, pageable: Pageable): Page<Product>

}