package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.collaborations

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCollaborationNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingLists/{id}/collaborations")
class UpdateCollaborations(
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,

        private val notificationService: NotificationService,

        private val emailService: EmailService,

        @Value("\${app.name}")
        private val applicationName: String
) {

    @PostMapping()
    @RolesAllowed(User.USER)
    fun updateCollaborations(
            @PathVariable id: Long,
            @AppUser user: User,
            @RequestBody @Valid update: List<UpdateCollaborationsDTO>
    ): ResponseEntity<ShoppingList> = try {

        val list = shoppingListDAO.findById(id)
        if (list.canUserEditCollaborations(user)) {

            update.forEach { it ->
                val c = shoppingListCollaborationDAO.findById(it.collaborationId!!)

                if (c.role != it.role) {
                    c.role = it.role!! // TODO: Verify!

                    shoppingListCollaborationDAO.save(c)

                    sendEmailToCollaborator(list, c.user)

                    sendNotificationToCollaborator(user, c)
                }
            }

            ResponseEntity(list, HttpStatus.OK)

        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

    } catch (ex: ShoppingListNotFoundException) {
        ResponseEntity.notFound().build()
    } catch (ex: ShoppingListCollaborationNotFoundException) {
        ResponseEntity.notFound().build()
    }

    data class UpdateCollaborationsDTO(
            @get:NotNull
            val collaborationId: Long?,

            @get:NotNull
            @get:NotEmpty
            val role: String?
    )


    private fun sendEmailToCollaborator(
            list: ShoppingList,
            user: User
    ) {
        // TODO: Improve email
        emailService.sendEmail(object : Email() {
            override fun to() = user.email

            override fun subject() = "$applicationName - Sei stato potenziato"

            override fun text() = "Ora hai più permessi"
        })
    }

    private fun sendNotificationToCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ha cambiato i tuoi privilegi nella lista \"${list.name}\"",
            icon    = inviter.photo,
            target  = collaboration.user
        ))
    }
}