package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.RecoverPasswordEmail
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class RecoverPasswordController(
        private val userDAO: UserDAO,
        private val tokenDAO: VerificationTokenDAO,
        val emailService: EmailService
) {

    /**
     * Handles the request to start the recover of the password
     */
    @PostMapping("/{email}/recoverPassword")
    fun recoverPassword(
            @PathVariable @javax.validation.constraints.Email email: String
    ): ResponseEntity<Any> = try {

        val user = userDAO.getUserByEmail(email)
        val token = VerificationToken(user = user)
        tokenDAO.save(token)

        emailService.sendEmail(RecoverPasswordEmail(token))

        ok()
    } catch (ex: UserNotFoundException) {
        notFound()
    }

}