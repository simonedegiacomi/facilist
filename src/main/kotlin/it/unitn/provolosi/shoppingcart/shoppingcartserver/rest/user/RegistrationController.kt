package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.EmailAlreadyInUseException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.ConfirmAccountEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/users")
class RegistrationController (
        val userDAO: UserDAO,
        val tokenDAO: VerificationTokenDAO,
        val emailService: EmailService
) {

    @PostMapping("/register")
    fun registerUser(
            @RequestBody @Valid registration: RegisterDTO,
            req: HttpServletRequest
    ): ResponseEntity<User> = try {

        val user = userDAO.save(registration.toUser())

        val token = tokenDAO.save(VerificationToken(user = user))

        emailService.sendEmail(ConfirmAccountEmail(token))

        ResponseEntity(user, HttpStatus.CREATED)
    } catch (ex: EmailAlreadyInUseException) {
        ResponseEntity.status(HttpStatus.CONFLICT).build()
    }

    data class RegisterDTO(

            @get:javax.validation.constraints.Email
            val email: String,

            @get:NotEmpty
            val firstName: String,

            @get:NotEmpty
            val lastName: String,

            @get:Password
            val password: String) {

        fun toUser() = User(
            email       = email,
            firstName   = firstName,
            lastName    = lastName,
            password    = BCryptPasswordEncoder().encode(password)
        )
    }
}
