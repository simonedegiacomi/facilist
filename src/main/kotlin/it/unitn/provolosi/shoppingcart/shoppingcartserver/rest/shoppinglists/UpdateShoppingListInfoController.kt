package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import org.springframework.http.HttpStatus
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
        private val syncShoppingListService: SyncService,
        private val shoppingListNotifications: ShoppingListNotifications
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun updateInfo(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid update: UpdateShoppingListDTO
    ): ResponseEntity<ShoppingList> = if (list.canUserEditList(user)) {

        list.apply {
            name        = update.name!!
            description = update.description
            icon        = update.icon!!
        }

        shoppingListDAO.save(list)

        syncShoppingListService.shoppingListInfoEdited(list)

        shoppingListNotifications.notifyCollaboratorsListUpdated(user, list)

        ResponseEntity.ok(list)
    } else {
        ResponseEntity.status(HttpStatus.FORBIDDEN).build()
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