package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.IShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ConfirmRegistration(
        private val tokenDAO: VerificationTokenDAO,
        private val userDAO: UserDAO,
        private val shoppingListService: IShoppingListService
) {

    @PostMapping("/users/verifyEmail/{email}")
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
                onNewUserActivated(user)

                ResponseEntity.ok().build()
            }
        }
    } catch (ex: VerificationTokenNotFoundException) {
        ResponseEntity.notFound().build()
    }

    private fun onNewUserActivated (user: User) = shoppingListService.acceptInvitesForUser(user)
}