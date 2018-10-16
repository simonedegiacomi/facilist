package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.products

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductDAO
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

        if (user.role == User.USER) {
            product.creator = user
        }

        productDAO.save(product)

        ResponseEntity(product, HttpStatus.CREATED)
    } catch (ex: ProductCategoryNotFoundException) {

        ResponseEntity.notFound().build()
    }


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
