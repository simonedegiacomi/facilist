package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategories

import conflict
import created
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid

@RestController
@RequestMapping("/api/productCategories")
class CreateController(
        private val productCategoryDAO: ProductCategoryDAO
) {

    /**
     * Handles the request to create a new ProductCategory
     */
    @PostMapping
    @RolesAllowed(User.ADMIN)
    fun create(@RequestBody @Valid category: ProductCategory) =
            if (productCategoryDAO.existsWithName(category.name)) {
                conflict()
            } else {
                productCategoryDAO.save(category)
                created(category)
            }
}