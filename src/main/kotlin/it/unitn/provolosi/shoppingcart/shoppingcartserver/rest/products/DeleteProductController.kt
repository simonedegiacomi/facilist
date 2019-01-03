package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class DeleteProductController(
        private val productDAO: ProductDAO
) {


    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable id: Long,
            @AppUser user: User
    ): ResponseEntity<Any> = try {
        val product = productDAO.findById(id)
        if (!product.canBeEditedBy(user)) {
            forbidden()

        } else {

            productDAO.deleteById(id)

            ok()
        }
    } catch (ex: ProductNotFoundException) {
        notFound()
    }

}