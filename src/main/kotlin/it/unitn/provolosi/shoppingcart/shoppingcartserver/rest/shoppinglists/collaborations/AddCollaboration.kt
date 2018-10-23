package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.IShoppingListService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.InviterCantEditCollaboratorsException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("/shoppingLists/{id}/collaborations")
class AddCollaboration (
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListService: IShoppingListService
) {

    @PutMapping()
    @RolesAllowed(User.USER)
    fun addCollaborator(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid add: AddCollaboratorDTO,
            req: HttpServletRequest
    ): ResponseEntity<ShoppingList> = try {

        val list = shoppingListDAO.findById(id)

        shoppingListService.addUserToShoppingListByEmail(list, user, add.email!!, req)

        ResponseEntity(list, HttpStatus.OK)
    } catch (ex: ShoppingListNotFoundException) {

        ResponseEntity.notFound().build()
    } catch (ex: InviterCantEditCollaboratorsException) {

        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    data class AddCollaboratorDTO (


            @get:NotNull()
            @get:javax.validation.constraints.Email()
            val email: String?,

            val id: Long? // NOTE: Do not remove this unused field! TODO: Find out why it doesn't work without
    )

}