package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ISyncShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingLists")
class DeleteShoppingList(
        private val shoppingListDAO: ShoppingListDAO,
        private val updatesService: ISyncShoppingListService,
        private val notificationService: NotificationService
) {

    @DeleteMapping("/{shoppingListId}")
    @RolesAllowed(User.USER)
    fun delete(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User
    ): ResponseEntity<Any> =
        if (list.creator == user) {
            shoppingListDAO.delete(list)

            updatesService.shoppingListDeleted(list)

            sendNotificationToCollaborators(user, list)

            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }


    private fun sendNotificationToCollaborators(user: User, list: ShoppingList) {
        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != user }
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha eliminato la lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo
                    )
                }

        notificationService.saveAndSend(notifications)
    }
}