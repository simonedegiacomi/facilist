package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import badRequest
import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.ShoppingListNotifications
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.WebSocketSyncService
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/collaborations")
class UpdateCollaborationsController(
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val shoppingListNotifications: ShoppingListNotifications,
        private val syncShoppingListService: WebSocketSyncService
) {

    /**
     * Updates the collaboration of a user (his roles).
     * This request expect all the colaborations to be sent
     */
    @PostMapping()
    @RolesAllowed(User.USER)
    fun updateCollaborations(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid update: List<UpdateCollaborationsDTO>
    ): ResponseEntity<ShoppingList> {

        if (!areRolesValid(update)) {
            return badRequest()
        }

        try {
            if (!list.canUserEditCollaborations(user)) {
                return forbidden()
            }

            update.forEach { it ->
                // get the current collaboration
                val c = shoppingListCollaborationDAO.findById(it.collaborationId!!)

                // Check if the role was changed
                if (c.role != it.role) {

                    // Update the role in the database
                    c.role = it.role!!
                    shoppingListCollaborationDAO.save(c)

                    // Sync the clients
                    syncShoppingListService.collaborationEdited(c)

                    // Notify the collaborator
                    shoppingListNotifications.notifyCollaboratorRoleChanged(user, c)
                }
            }

            return ok(list)

        } catch (ex: ShoppingListCollaborationNotFoundException) {
            return notFound()
        }
    }

    data class UpdateCollaborationsDTO(
            @get:NotNull
            val collaborationId: Long?,

            @get:NotNull
            @get:NotEmpty
            val role: String?
    )

    /**
     * Checks if the roles in the updates list (which are strings) represent valid roles
     */
    private fun areRolesValid (update: List<UpdateCollaborationsDTO>) = update.map { it.role!! }
                    .all { ShoppingListCollaboration.isRoleValid(it) }

}