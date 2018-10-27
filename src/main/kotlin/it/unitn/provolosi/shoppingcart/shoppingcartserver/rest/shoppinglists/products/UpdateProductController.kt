package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncShoppingListService
import notFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@RestController
@RequestMapping("/api/shoppingListProducts/{id}")
class UpdateProductController(
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val syncShoppingListService: SyncShoppingListService
) {

    @PutMapping
    fun updateProduct(
            @AppUser user: User,
            @PathVariable id: Long,
            @RequestBody @Valid update: UpdateShoppingListProductDTO
    ): ResponseEntity<ShoppingListProduct> = try {
        val relation = shoppingListProductDAO.findById(id)
        val list = relation.shoppingList

        if (list.isUserOwnerOrCollaborator(user)) {

            relation.apply {
                quantity    = update.quantity!!
                image       = update.image!!
                bought      = update.bought!!
                note        = update.note
            }

            syncShoppingListService.productInShoppingListEdited(relation)

            ResponseEntity.ok(relation)

        } else {
            forbidden()
        }

    } catch (ex: EntityNotFoundException) {
        notFound()
    }

    data class UpdateShoppingListProductDTO(
            @get:NotNull()
            @get:NotEmpty()
            val image: String?,

            val note: String?,

            @get:NotNull()
            val bought: Boolean?,

            @get:NotNull()
            @get:PositiveOrZero()
            val quantity: Int?
    )
}
