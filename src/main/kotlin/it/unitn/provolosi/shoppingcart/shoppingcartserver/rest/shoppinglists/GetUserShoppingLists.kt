package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
class GetUserShoppingLists (
        private val shoppingListDAO: ShoppingListDAO
) {

    @GetMapping("/users/me/shoppingLists")
    @RolesAllowed(User.USER)
    fun getUserShoppingLists (
            @AppUser user: User
    ) = ResponseEntity(shoppingListDAO.getShoppingListPreviewsByUser(user), HttpStatus.OK)

}