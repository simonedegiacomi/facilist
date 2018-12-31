package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/products/search")
class SearchProductController (
        private val productDAO: ProductDAO
) {

    /**
     * Handler of the request to search product by name.
     * This request can be call only by admin users
     */
    @GetMapping(params = ["name"])
    @RolesAllowed(User.ADMIN)
    fun searchByName(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseAndCreatedByAdminOrderByName(name, PageRequest.of(page, size)),
        HttpStatus.OK
    )

    /**
     * Handler of the request to search product by category.
     * This request can be call only by admin users.
     */
    @GetMapping(params = ["categoryId"])
    @RolesAllowed(User.ADMIN)
    fun searchByCategory(
            @RequestParam(name = "categoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByCategoryIdAndCreatedByAdminOrderByName(categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )

    /**
     * Handler of the request to search product by name and category.
     * This request can be call only by admin users.
     */
    @GetMapping(params = ["name", "categoryId"])
    @RolesAllowed(User.ADMIN)
    fun searchByNameAndCategory(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "categoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseAndCategoryIdAndCreatedByAdminOrderByName(name, categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )


    /**
     * Handle the search for products to add to a shopping list. This means the products returned can be insert in a
     * shopping list of the specified category (that is, the categories of the products belong to the shopping list).
     * If the 'includeUserProducts' is set to true, also products created by the user (and of the right category) are
     * returned.
     */
    @GetMapping(params = ["name", "shoppingListCategoryId", "includeUserProducts"])
    @RolesAllowed(User.USER)
    fun searchByNameAndShoppingListCategoryIncludingUserProducts(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "shoppingListCategoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int,
            @RequestParam(name = "includeUserProducts", defaultValue = "true") includeUserProduct: Boolean,
            @AppUser user: User
    ): ResponseEntity<Page<Product>> =  if (includeUserProduct){
        ResponseEntity(
            productDAO.findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrUserOrderByName(name, categoryId, user, PageRequest.of(page, size)),
            HttpStatus.OK
        )
    } else {
        searchByNameAndShoppingListCategory(name, categoryId, page, size)
    }

    /**
     * Handle the search for products to add to a shopping list.
     * Same as {@link searchByNameAndShoppingListCategoryIncludingUserProducts} with the 'includeUserProducts' parameter
     * set to false , but can be called even if the user isn't legged in.
     */
    @GetMapping(params = ["name", "shoppingListCategoryId"])
    @PermitAll
    fun searchByNameAndShoppingListCategory(
            @RequestParam(name = "name", defaultValue = "") name: String,
            @RequestParam(name = "shoppingListCategoryId", defaultValue = "") categoryId: Long,
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int
    ): ResponseEntity<Page<Product>> = ResponseEntity(
        productDAO.findByNameContainingIgnoreCaseAndShoppingListCategoryIdAndCreatedByAdminOrderByName(name, categoryId, PageRequest.of(page, size)),
        HttpStatus.OK
    )

}