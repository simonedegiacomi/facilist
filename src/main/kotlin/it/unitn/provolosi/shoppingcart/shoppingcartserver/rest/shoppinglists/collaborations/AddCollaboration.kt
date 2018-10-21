package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.persistence.EntityNotFoundException
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("/shoppingLists/{id}/collaborations")
class AddCollaboration (
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val userDAO: UserDAO
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun addCollaborator(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid add: AddCollaboratorDTO
    ): ResponseEntity<ShoppingList> = try {

        val list = shoppingListDAO.findById(id)
        if (list.canUserEditCollaborations(user)) {

            addUserByEmailToList(list, add.email!!)

            ResponseEntity(list, HttpStatus.OK)
        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    }

    data class AddCollaboratorDTO (


            @get:NotNull()
            @get:Email()
            val email: String?,

            val id: Long? // NOTE: Do not remove this unused field! TODO: Find out why it doesn't work without
    )


    private fun addUserByEmailToList(list: ShoppingList, email: String) = try {
        val user = userDAO.getUserByEmail(email)

        addUserToList(list, user)

    } catch (ex: EntityNotFoundException) {

        inviteUserByEmailToList(list, email)
    }


    private fun addUserToList(list: ShoppingList, user: User) = shoppingListCollaborationDAO.save(
        ShoppingListCollaboration(
            user = user,
            shoppingList = list
        )
    )

    private fun inviteUserByEmailToList(list: ShoppingList, email: String) {
        // TODO: Implement
    }
}