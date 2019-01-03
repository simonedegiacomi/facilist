package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ShoppingListProductsUpdateService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import notFound
import ok
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
        private val syncShoppingListService: WebSocketSyncService,
        private val shoppingListProductsUpdateService: ShoppingListProductsUpdateService
) {

    /**
     * Handle the request to update a product in a list (notes, images, quantity, to buy or not, etc...)
     */
    @PutMapping
    fun updateProduct(
            @AppUser user: User,
            @PathVariable id: Long,
            @RequestBody @Valid update: UpdateShoppingListProductDTO
    ): ResponseEntity<ShoppingListProduct> = try {
        val relation = shoppingListProductDAO.findById(id)
        val list = relation.shoppingList

        if (list.isUserOwnerOrCollaborator(user)) {

            // Update the database
            relation.apply {
                quantity    = update.quantity!!
                image       = update.image!!
                bought      = update.bought!!
                note        = update.note
            }
            shoppingListProductDAO.save(relation)

            // Sync clients
            syncShoppingListService.productInShoppingListEdited(relation)

            // Generate the event to group
            shoppingListProductsUpdateService.collectEvent(user, relation)

            ok(relation)

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
