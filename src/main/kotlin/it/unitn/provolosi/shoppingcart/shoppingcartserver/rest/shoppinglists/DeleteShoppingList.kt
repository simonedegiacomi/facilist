package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ISyncShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingLists")
class DeleteShoppingList(
        private val shoppingListDAO: ShoppingListDAO,
        private val updatesService: ISyncShoppingListService
) {

    @DeleteMapping("/{id}")
    @RolesAllowed(User.USER)
    fun delete(
            @PathVariable id: Long,
            @AppUser user: User
    ): ResponseEntity<Any> = try {
        val list = shoppingListDAO.findById(id)
        if (list.creator == user) {
            shoppingListDAO.delete(list)

            updatesService.shoppingListDeleted(list)

            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }
}