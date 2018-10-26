package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/productCategories/search")
class SearchController(
        private val productCategoryDAO: ProductCategoryDAO
) {

    @GetMapping("/byName")
    fun searchByName(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ):ResponseEntity<Page<ProductCategory>> = ResponseEntity(
        productCategoryDAO.findByNameContainingIgnoreCase(name, PageRequest.of(page, size)),
        HttpStatus.OK
    )
}