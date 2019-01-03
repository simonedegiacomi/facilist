package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}")
class UpdateShoppingListInfoController(
        private val shoppingListDAO: ShoppingListDAO,
        private val syncShoppingListService: WebSocketSyncService,
        private val shoppingListNotifications: ShoppingListNotifications
) {

    /**
     * Handles the request to update the description, name and icon of a shopping list
     */
    @PutMapping()
    @RolesAllowed(User.USER)
    fun updateInfo(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid update: UpdateShoppingListDTO
    ): ResponseEntity<ShoppingList> = if (list.canUserEditList(user)) {
        // update the database
        list.apply {
            name        = update.name!!
            description = update.description
            icon        = update.icon!!
        }
        shoppingListDAO.save(list)

        // sync clients
        syncShoppingListService.shoppingListInfoEdited(list)

        shoppingListNotifications.notifyCollaboratorsListUpdated(user, list)

        ok(list)
    } else {
        forbidden()
    }

    data class UpdateShoppingListDTO(

            @get:NotNull()
            @get:NotEmpty()
            val name: String?,

            val description: String?,

            @get:NotNull()
            @get:NotEmpty()
            val icon: String?

    )

}