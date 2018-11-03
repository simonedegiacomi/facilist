package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncShoppingListService
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
        private val syncShoppingListService: SyncShoppingListService,
        private val notificationService: NotificationService
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
            description = update.description!!
            icon        = update.icon!!
        }

        shoppingListDAO.save(list)

        syncShoppingListService.shoppingListInfoEdited(list)

        sendNotificationToCollaborators(user, list)

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


    private fun sendNotificationToCollaborators(user: User, list: ShoppingList) {
        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != user }
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha modificato la lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo
                    )
                }

        notificationService.saveAndSend(notifications)
    }


}