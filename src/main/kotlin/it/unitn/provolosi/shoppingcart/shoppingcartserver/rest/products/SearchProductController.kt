package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
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
@RequestMapping("/api/products/search")
class SearchProductController (
        private val productDAO: ProductDAO
) {

    @GetMapping(params = ["name"])
    fun searchByName(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseOrderByName(name, PageRequest.of(page, size)),
        HttpStatus.OK
    )

    @GetMapping(params = ["categoryId"])
    fun searchByCategory(
            @RequestParam(name = "categoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByCategoryIdOrderByName(categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )

    @GetMapping(params = ["name", "categoryId"])
    fun searchByNameAndCategory(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "categoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseAndCategoryIdOrderByName(name, categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )


    @GetMapping(params = ["name", "shoppingListCategoryId"])
    fun searchByNameAndShoppingListCategory(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "shoppingListCategoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseAndShoppingListCategoryIdOrderByName(name, categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )

}