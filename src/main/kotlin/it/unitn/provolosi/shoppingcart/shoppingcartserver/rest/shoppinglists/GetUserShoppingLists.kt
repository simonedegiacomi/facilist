package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/shoppingLists/{id}")
    @RolesAllowed(User.USER)
    fun getById(
            @PathVariable id: Long,
            @AppUser user: User
    ): ResponseEntity<ShoppingList> = try{
        val list = this.shoppingListDAO.findById(id)

        if (list.isUserOwnerOrCollaborator(user)) {
            ResponseEntity(list, HttpStatus.OK)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }

}