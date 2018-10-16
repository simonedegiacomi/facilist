package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InternalSpringJPAProductDAO : JpaRepository<Product, Long> {
    fun findAllByOrderByNameAsc(page: Pageable): Page<Product>
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
}