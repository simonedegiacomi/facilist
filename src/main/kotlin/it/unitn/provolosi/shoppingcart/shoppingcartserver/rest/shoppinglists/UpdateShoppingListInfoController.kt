package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingLists/{id}")
class UpdateShoppingListInfoController(
        private val shoppingListDAO: ShoppingListDAO,
        private val syncShoppingListService: SyncShoppingListService
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun updateInfo(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid update: UpdateShoppingListDTO
    ): ResponseEntity<ShoppingList> = try {

        val list = shoppingListDAO.findById(id)

        if (list.canUSerEditList(user)) {

            list.apply {
                name        = update.name!!
                description = update.description!!
                icon        = update.icon!!
            }

            shoppingListDAO.save(list)

            syncShoppingListService.shoppingListInfoEdited(list)

            ResponseEntity.ok(list)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }

    data class UpdateShoppingListDTO(

            @get:NotNull()
            @get:NotEmpty()
            val name: String?,

            @get:NotNull()
            @get:NotEmpty()
            val description: String?,


            @get:NotNull()
            @get:NotEmpty()
            val icon: String?

    )

}