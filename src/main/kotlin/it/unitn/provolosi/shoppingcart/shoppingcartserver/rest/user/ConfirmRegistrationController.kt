package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import notFound
import ok
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ConfirmRegistrationController(
        private val tokenDAO: VerificationTokenDAO,
        private val userDAO: UserDAO,
        private val inviteToJoinDAO: InviteToJoinDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO
) {

    @PostMapping("/api/users/verifyEmail/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun verifyToken(
            @PathVariable email: String,
            @RequestParam("token") tokenString: String
    ): ResponseEntity<Any> = try {
        val token = tokenDAO.findByToken(tokenString)

        val user = token.user

        when {
            user.email != email -> ResponseEntity.badRequest().build()

            user.emailVerified -> ResponseEntity.ok().build()

            else -> {
                user.emailVerified = true

                userDAO.save(user)
                tokenDAO.delete(token)
                acceptInvitesForUser(user)

                ok()
            }
        }
    } catch (ex: VerificationTokenNotFoundException) {
        notFound()
    }


    fun acceptInvitesForUser(user: User) = inviteToJoinDAO
            .findByEmail(user.email)
            .forEach { invite ->
                shoppingListCollaborationDAO.save(ShoppingListCollaboration(
                    user            = user,
                    shoppingList    = invite.shoppingList
                ))
            }
}