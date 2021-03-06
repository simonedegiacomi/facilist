package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/productCategories")
class DeleteController(
        private val productCategoryDAO: ProductCategoryDAO
) {

    @DeleteMapping("/{id}")
    @RolesAllowed(User.ADMIN)
    fun edit(
            @PathVariable id: Long
    ) = try {
        productCategoryDAO.deleteById(id)
        ResponseEntity.status(HttpStatus.NO_CONTENT).build<Any>()
    } catch (ex: ProductCategoryNotFoundException) {
        ResponseEntity.notFound().build<Any>()
    }
}