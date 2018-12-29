package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ISyncService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingLists")
class DeleteShoppingListController(
        private val shoppingListDAO: ShoppingListDAO,
        private val updatesService: ISyncService,
        private val shoppingListNotifications: ShoppingListNotifications
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

            shoppingListNotifications.notifyCollaboratorsListDeleted(user, list)

            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
}