package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
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
        private val notificationService: NotificationService,
        private val syncShoppingListService: SyncService,


        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    @PostMapping()
    @RolesAllowed(User.USER)
    fun updateCollaborations(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @RequestBody @Valid update: List<UpdateCollaborationsDTO>
    ): ResponseEntity<ShoppingList> {

        if (!areRolesValid(update)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        try {
            if (!list.canUserEditCollaborations(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            update.forEach { it ->
                val c = shoppingListCollaborationDAO.findById(it.collaborationId!!)

                if (c.role != it.role) {
                    c.role = it.role!!

                    shoppingListCollaborationDAO.save(c)

                    syncShoppingListService.collaborationEdited(c)
                    sendNotificationToCollaborator(user, c)
                }
            }

            return ResponseEntity(list, HttpStatus.OK)

        } catch (ex: ShoppingListCollaborationNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }

    data class UpdateCollaborationsDTO(
            @get:NotNull
            val collaborationId: Long?,

            @get:NotNull
            @get:NotEmpty
            val role: String?
    )

    private fun areRolesValid (update: List<UpdateCollaborationsDTO>) = update.map { it.role!! }
                    .all { ShoppingListCollaboration.isRoleValid(it) }

    private fun sendNotificationToCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ha cambiato i tuoi privilegi nella lista \"${list.name}\"",
            icon = inviter.photo,
            target = collaboration.user,
            url = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }
}