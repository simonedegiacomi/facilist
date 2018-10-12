package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.awt.print.Pageable

interface SpringJPAProductCategory : ProductCategoryDAO, JpaRepository<ProductCategory, Long> {

    @Query("SELECT COUNT(pc) > 0 from ProductCategory pc where pc.name = :name")
    override fun existsWithName(name: String): Boolean
}
