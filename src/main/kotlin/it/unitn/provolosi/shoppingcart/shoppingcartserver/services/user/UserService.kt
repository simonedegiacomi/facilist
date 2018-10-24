package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import protocolPortAndDomain
import javax.servlet.http.HttpServletRequest

@Component
class UserService(
        private val userDAO: UserDAO,
        private val changePasswordRequestDAO: ChangePasswordRequestDAO,
        private val emailService: EmailService
) : IUserService {

    @Bean("passwordEncoder")
    override fun passwordEncoder() = BCryptPasswordEncoder()

    override fun changeUserPassword(
            user: User,
            currentPassword: String,
            newPassword: String
    ) = if (passwordEncoder().matches(currentPassword, user.password)) {
        user.password = passwordEncoder().encode(newPassword)
        userDAO.save(user)
    } else {
        throw WrongCurrentPasswordException()
    }


    override fun changeUserEmail(user: User, email: String, req: HttpServletRequest): ChangePasswordRequest {
        val request = changePasswordRequestDAO.save(ChangePasswordRequest(
            token       = VerificationToken(user = user),
            newEmail    = email
        ))

        sendChangeEmailAddressEmail(request, req)

        return request
    }

    private fun sendChangeEmailAddressEmail(
            change: ChangePasswordRequest,
            req: HttpServletRequest
    ) {
        val verificationLink = generateConfirmationLink(change, req)
        emailService.sendEmail(object : Email() {
            override fun to() = change.newEmail

            override fun subject() = "Cambia la tua email"

            override fun text() = "Conferma il tuo indirizzo email usando questo link: $verificationLink"
        })
    }


    private fun generateConfirmationLink(change: ChangePasswordRequest, req: HttpServletRequest) = UriComponentsBuilder
            .fromHttpUrl("${req.protocolPortAndDomain()}/verifyNewEmail/{email}")
            .queryParam("token", change.token.token)
            .buildAndExpand(mapOf("email" to change.newEmail))
            .toUriString()


    override fun confirmChangeUserEmail(email: String, tokenString: String): User {
        val req     = changePasswordRequestDAO.findByNewEmailAndTokenByToken(email, tokenString)
        val user    = req.token.user

        user.email = email
        userDAO.save(user)
        changePasswordRequestDAO.delete(req)

        SecurityContextHolder.clearContext()

        return user
    }
}