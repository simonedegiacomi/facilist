package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.htmlemailengine.HTMLEmailEngine
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import protocolPortAndDomain
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class RecoverPasswordController(
        private val userDAO: UserDAO,
        private val tokenDAO: VerificationTokenDAO,
        val emailService: EmailService,
        val htmlEmailEngine: HTMLEmailEngine,

        @Value("\${app.name}")
        private val applicationName: String
) {

    @PostMapping("/{email}/recoverPassword")
    fun recoverPassword(
            @PathVariable @javax.validation.constraints.Email email: String,
            req: HttpServletRequest
    ): ResponseEntity<Any> = try {

        val user = userDAO.getUserByEmail(email)
        val token = VerificationToken(user = user)
        tokenDAO.save(token)

        sendRecoverEmail(token, req)

        ResponseEntity.ok().build()
    } catch (ex: UserNotFoundException) {
        ResponseEntity.notFound().build()
    }


    private fun sendRecoverEmail(token: VerificationToken, req: HttpServletRequest) {
        val user = token.user
        val verificationLink = generateConfirmationLink(token, req)

        emailService.sendEmail(object : Email(){
            override fun to() = user.email

            override fun subject() = "$applicationName - Password dimenticata"

            override fun text() = "Recupera il tuo account cambiando la password, clicca sul link: $verificationLink"

            override fun html() = htmlEmailEngine.render("recover-password", mapOf(
                "applicationName" to applicationName,
                "link" to verificationLink
            ))
        })
    }

    private fun generateConfirmationLink(token: VerificationToken, req: HttpServletRequest) = UriComponentsBuilder
            .fromHttpUrl("${req.protocolPortAndDomain()}/recoverPassword/{email}")
            .queryParam("token", token.token)
            .buildAndExpand(mapOf("email" to token.user.email))
            .toUriString()
}