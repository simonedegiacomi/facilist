package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import created
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import notFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingLists")
class CreateShoppingListController (
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO,
        private val syncService: SyncService
) {

    /**
     * Handles the request to create a new shopping list
     */
    @PostMapping
    @RolesAllowed(User.USER)
    fun create(
            @AppUser user: User,
            @RequestBody @Valid dto: CreateShoppingListDTO
    ): ResponseEntity<ShoppingList> = try {
        // Create the entity in yhe database
        val list = shoppingListDAO.save(ShoppingList(
            name        = dto.name!!,
            description = dto.description,
            icon        = dto.icon!!,
            creator     = user,
            category    = shoppingListCategoryDAO.findById(dto.shoppingListCategoryId!!)
        ))

        // sync the clients
        syncService.userNewShoppingList(user, list)

        created(list)
    } catch (ex: ShoppingListCategoryNotFoundException) {
        notFound()
    }

    data class CreateShoppingListDTO(

            @get:NotNull
            @get:NotEmpty
            val name: String?,

            val description: String?,

            @get:NotNull
            @get:NotEmpty
            val icon: String?,

            @get:NotNull
            val shoppingListCategoryId: Long?
    )
}