package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/shoppingLists")
class UpdateProducts(
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val productDAO: ProductDAO
) {

    @PostMapping("/{id}/products")
    @RolesAllowed(User.USER)
    fun updateProducts(
            @AppUser user: User,
            @PathVariable id: Long,
            @RequestBody @Valid update: List<ProductInListDTO>
    ): ResponseEntity<ShoppingList> = try {
        val list = shoppingListDAO.findById(id)

        if (list.isUserOwnerOrCollaborator(user)) {
            shoppingListProductDAO.deleteAll(list.products)
            list.products.clear()

            list.products.addAll(update.map { it ->
                shoppingListProductDAO.save(ShoppingListProduct(
                    product         = productDAO.findById(it.productId!!),
                    shoppingList    = list,
                    toBuy           = it.toBuy!!,
                    quantity        = it.quantity!!,
                    image           = it.image!!
                ))
            })

            ResponseEntity(list, HttpStatus.OK)

        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    } catch (ex: ShoppingListNotFoundException) {

        ResponseEntity.notFound().build()
    } catch (ex: ProductNotFoundException) {

        ResponseEntity.notFound().build()
    }

    data class ProductInListDTO(

            @get:NotNull
            val productId: Long?,

            @get:NotNull
            val quantity: Int?,

            @get:NotNull
            val toBuy: Boolean?,

            val note: String?,

            @get:NotNull
            val image: String?
    )
}