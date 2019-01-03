package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import created
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Product
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import notFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * HAndlers of the requests to create products
 */
@RestController
@RequestMapping("/api/products")
class CreateProductController (
        private val productDAO: ProductDAO,
        private val productCategoryDAO: ProductCategoryDAO
){

    @PostMapping()
    fun create (
            @RequestBody @Valid dto: CreateProductDTO,
            @AppUser user: User
    ): ResponseEntity<Product> = try {

        val product = Product(
            name        = dto.name!!,
            icon        = dto.icon!!,
            category    = productCategoryDAO.findById(dto.categoryId!!)
        )

        // Set the creator only if the user it's not an admin (to make the product accessible to all the users)
        if (user.role == User.USER) {
            product.creator = user
        }

        productDAO.save(product)

        created(product)
    } catch (ex: ProductCategoryNotFoundException) {

        notFound()
    }

    /**
     * Object received through JSON with annotations for validation
     */
    data class CreateProductDTO (

        // NOTE: To use the spring validation annotations, the field must be nullable, otherwise an exception
        // will be thrown when some fields are missing in the json

            @get:NotNull
            @get:NotEmpty
            val name: String?,

            @get:NotNull
            @get:NotEmpty
            val icon: String?,

            @get:NotNull
            val categoryId: Long?
    )


}
