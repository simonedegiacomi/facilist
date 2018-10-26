package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.EmailAlreadyInUseException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.htmlemailengine.HTMLEmailEngine
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import protocolPortAndDomain
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/users")
class RegistrationController (
        val userDAO: UserDAO,
        val tokenDAO: VerificationTokenDAO,
        val emailService: EmailService,
        val htmlEmailEngine: HTMLEmailEngine,

        @Value("\${app.name}")
        private val applicationName: String
) {

    @PostMapping("/register")
    fun registerUser(
            @RequestBody @Valid registration: RegisterDTO,
            req: HttpServletRequest
    ): ResponseEntity<User> = try {

        val user = registration.toUser()

        userDAO.save(user)

        val token = tokenDAO.save(VerificationToken(user = user))
        sendConfirmationEmailAddressEmail(token, req)

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


    private fun sendConfirmationEmailAddressEmail(token: VerificationToken, req: HttpServletRequest) {
        val user = token.user
        val verificationLink = generateConfirmationLink(token, req)

        emailService.sendEmail(object : Email() {
            override fun to() = token.user.email

            override fun subject() = "$applicationName - Conferma il tuo account!"

            override fun text() = "Conferma il tuo indirizzo email usando questo link: $verificationLink"

            override fun html() = htmlEmailEngine.render("verify-email", mapOf(
                "applicationName" to applicationName,
                "firstName" to user.firstName,
                "link" to verificationLink
            ))
        })
    }

    private fun generateConfirmationLink(token: VerificationToken, req: HttpServletRequest) = UriComponentsBuilder
            .fromHttpUrl("${req.protocolPortAndDomain()}/verifyEmail/{email}")
            .queryParam("token", token.token)
            .buildAndExpand(mapOf("email" to token.user.email))
            .toUriString()

}
