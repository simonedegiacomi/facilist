package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/products")
class EditProductController(
        private val productDAO: ProductDAO
) {


    @PutMapping("/{id}")
    fun edit(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid update: EditProductDTO
    ): ResponseEntity<Product> {

        return try {
            val product = productDAO.findById(id)

            if (!canUserEditProduct(user, product)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            } else {
                product.name = update.name!!
                product.icon = update.icon!!

                ResponseEntity(productDAO.save(product), HttpStatus.OK)
            }

        } catch (ex: ProductNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }


    data class EditProductDTO (

        // NOTE: To use the spring validation annotations, the field must be nullable, otherwise an exception
        // will be thrown when some fields are missing in the json

            @get:NotNull
            @get:NotEmpty
            val name: String?,

            @get:NotNull
            @get:NotEmpty
            val icon: String?
    )


    private fun canUserEditProduct(user: User, product: Product) =
            (user.isAdmin() && product.wasCreatedByAnAdmin()) || product.wasCreatedBy(user)
}