package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid

@RestController
@RequestMapping("/productCategories")
class CreateController(
        private val productCategoryDAO: ProductCategoryDAO
) {

    @PostMapping
    @RolesAllowed(User.ADMIN)
    fun create(@RequestBody @Valid category: ProductCategory) =
            if (productCategoryDAO.existsWithName(category.name)) {
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } else {
                productCategoryDAO.save(category)
                ResponseEntity(category, HttpStatus.CREATED)
            }
}