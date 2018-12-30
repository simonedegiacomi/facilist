package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/productCategories")
class GetController(
        private val productCategoryDAO: ProductCategoryDAO
) {

    @GetMapping(params = ["page", "size"])
    fun getAllPaginated(
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ) = ResponseEntity.ok(productCategoryDAO.findAllByOrderByNameAsc(PageRequest.of(page, size)))


    @GetMapping()
    fun getAll() = ResponseEntity.ok(
        productCategoryDAO.findAllWithProductsCountByOrderByNameAsc()
                .map { CategoryWithProductCount.fromPair(it) }
    )
}

data class CategoryWithProductCount(

        val id: Long,
        val name: String,
        val description: String,
        val icon: String,
        val productsCount: Long
) {
    companion object {
        fun fromPair(pair: Pair<ProductCategory, Long>) = CategoryWithProductCount(
            pair.first.id!!,
            pair.first.name,
            pair.first.description,
            pair.first.icon,
            pair.second
        )
    }
}