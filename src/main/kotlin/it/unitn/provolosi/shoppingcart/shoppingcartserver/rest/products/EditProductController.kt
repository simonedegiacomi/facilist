package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import notFound
import ok
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/products")
class EditProductController(
        private val productDAO: ProductDAO
) {

    /**
     * Request to edit a product information (icon and name)
     */
    @PutMapping("/{id}")
    fun edit(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid update: EditProductDTO
    ): ResponseEntity<Product> = try {
        val product = productDAO.findById(id)

        if (!product.canBeEditedBy(user)) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } else {
            product.name = update.name!!
            product.icon = update.icon!!

            ok(productDAO.save(product))
        }

    } catch (ex: ProductNotFoundException) {
        notFound()
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
}