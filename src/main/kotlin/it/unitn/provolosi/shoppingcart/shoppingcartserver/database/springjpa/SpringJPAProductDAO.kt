package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

interface InternalSpringJPAProductDAO : JpaRepository<Product, Long> {
    fun findAllByOrderByNameAsc(page: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndCreatorIsNullOrderByName(name: String, pageable: Pageable): Page<Product>

    fun findByNameContainingIgnoreCaseAndCategoryIdAndCreatorIsNullOrderByName(name: String, categoryId: Long, pageable: Pageable): Page<Product>

    fun findByCategoryIdAndCreatorIsNullOrderByName(categoryId: Long, pageable: Pageable): Page<Product>

    @Query(
        "FROM Product p " +
                "WHERE lower(p.name) LIKE lower(concat('%', :name, '%')) AND " +
                "p.category IN (SELECT ELEMENTS(c.productCategories) FROM ShoppingListCategory c WHERE c.id = :id) AND " +
                "p.creator IS NULL " +
                "ORDER BY p.category.name, p.name")
    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatorIsNullOrderByName
                (name: String, id: Long, pageable: Pageable): Page<Product>

    @Query(
        "FROM Product p " +
                "WHERE lower(p.name) LIKE lower(concat('%', :name, '%')) AND " +
                "(p.creator IS NULL OR p.creator = :user) AND " +
                "p.category IN (SELECT ELEMENTS(c.productCategories) FROM ShoppingListCategory c WHERE c.id = :id) " +
                "ORDER BY p.category.name, p.name")
    fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatorIsNullOrUserOrderByName
                (name: String, id: Long, user: User,pageable: Pageable): Page<Product>
}

@Component
class SpringJPAProductDAO(
        @Autowired
        private val springRepository: InternalSpringJPAProductDAO
) : ProductDAO {

    override fun findAllByOrderByNameAsc(page: Pageable): Page<Product> = springRepository.findAllByOrderByNameAsc(page)

    override fun save(product: Product) = springRepository.save(product)

    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException) {
        throw ProductNotFoundException()
    }

    override fun findById(id: Long) = springRepository.findById(id).orElseThrow { ProductNotFoundException() }!!

    override fun findByNameContainingIgnoreCaseAndCreatedByAdminOrderByName(name: String, pageable: Pageable) =
            springRepository.findByNameContainingIgnoreCaseAndCreatorIsNullOrderByName(name, pageable)


    override fun findByNameContainingIgnoreCaseAndCategoryIdAndCreatedByAdminOrderByName
                (name: String, categoryId: Long, pageable: Pageable) =
            springRepository.findByNameContainingIgnoreCaseAndCategoryIdAndCreatorIsNullOrderByName(name, categoryId, pageable)

    override fun findByCategoryIdAndCreatedByAdminOrderByName(categoryId: Long, pageable: Pageable) =
            springRepository.findByCategoryIdAndCreatorIsNullOrderByName(categoryId, pageable)

    override fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrderByName
                (name: String, categoryId: Long, pageable: Pageable) =
            springRepository.findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatorIsNullOrderByName(name, categoryId, pageable)

    override fun findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrUserOrderByName
                (name: String, categoryId: Long, user: User, pageable: Pageable) =
            springRepository.findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatorIsNullOrUserOrderByName(name, categoryId, user, pageable)
}