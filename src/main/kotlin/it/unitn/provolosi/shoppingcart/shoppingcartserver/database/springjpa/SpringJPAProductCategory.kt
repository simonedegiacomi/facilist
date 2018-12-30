package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryWithSameNameAlreadyExistsException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

interface InternalSpringJPAProductCategory : JpaRepository<ProductCategory, Long> {

    @Query("SELECT COUNT(pc) > 0 from ProductCategory pc where pc.name = :name")
    fun existsWithName(name: String): Boolean

    fun findAllByOrderByNameAsc(page: Pageable): Page<ProductCategory>

    fun findAllByOrderByNameAsc(): List<ProductCategory>

    fun findByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageable: Pageable): Page<ProductCategory>

    @Query("SELECT pc, COUNT(p) FROM ProductCategory pc JOIN Product p ON p.category.id = pc.id GROUP BY pc.id")
    fun findAllWithProductsCountByOrderByNameAsc(): List<Array<Any>>
}

@Component
class SpringJPAProductCategory(

        @Autowired
        private val springRepository: InternalSpringJPAProductCategory
) : ProductCategoryDAO {

    override fun save(category: ProductCategory) = try {
        springRepository.save(category)

    } catch (ex: DataIntegrityViolationException) {
        if (ex.toString().contains(ProductCategory.PRODUCT_CATEGORY_UNIQUE_NAME_CONSTRAINT, true)) {
            throw ProductCategoryWithSameNameAlreadyExistsException()
        } else {
            throw RuntimeException("database error")
        }
    }

    override fun existsWithName(name: String) = springRepository.existsWithName(name)

    override fun findAllByOrderByNameAsc() = springRepository.findAllByOrderByNameAsc()

    override fun findAllWithProductsCountByOrderByNameAsc(): List<Pair<ProductCategory, Long>> =
            springRepository.findAllWithProductsCountByOrderByNameAsc()
                    .map { row -> Pair(row[0] as ProductCategory, row[1] as Long) }

    override fun findAllByOrderByNameAsc(page: Pageable) = springRepository.findAllByOrderByNameAsc(page)

    override fun findById(id: Long) = springRepository.findById(id).orElseThrow { ProductCategoryNotFoundException() }!!


    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException) {
        throw ProductCategoryNotFoundException()
    }


    override fun findByNameContainingIgnoreCase(name: String, pageable: Pageable) =
            springRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable)
}
