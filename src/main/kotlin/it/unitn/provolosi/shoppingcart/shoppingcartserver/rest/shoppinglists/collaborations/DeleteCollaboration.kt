package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/shoppingLists/{id}/collaborations")
class DeleteCollaboration(
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val userDAO: UserDAO
) {

    @DeleteMapping("{collaborationId}")
    @RolesAllowed(User.USER)
    fun deleteCollaboration(
            @PathVariable id: Long,
            @PathVariable collaborationId:Long,
            @AppUser user: User
    ): ResponseEntity<ShoppingList> = try {
        // TODO: DELETE products

        val list = shoppingListDAO.findById(id)
        if (list.canUserEditCollaborations(user)) {


            shoppingListCollaborationDAO.deleteById(collaborationId)

            ResponseEntity(list, HttpStatus.OK)
        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    } catch (ex: ShoppingListCollaborationNotFoundException) {
        ResponseEntity.notFound().build()
    }
}