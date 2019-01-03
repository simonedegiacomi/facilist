package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
class GetUserShoppingListsController(
        private val shoppingListDAO: ShoppingListDAO
) {

    @GetMapping("/api/users/me/shoppingLists")
    @RolesAllowed(User.USER)
    fun getUserShoppingLists(
            @AppUser user: User
    ) = ok(shoppingListDAO.getShoppingListPreviewsByUser(user))


    @GetMapping("/api/shoppingLists/{shoppingListId}")
    @RolesAllowed(User.USER)
    fun getById(
            @AppUser user: User,
            @PathVariableBelongingShoppingList list: ShoppingList
    ): ResponseEntity<ShoppingList> = ok(list)

}