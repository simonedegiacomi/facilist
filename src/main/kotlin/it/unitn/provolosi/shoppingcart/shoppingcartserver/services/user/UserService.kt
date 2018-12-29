package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.ConfirmNewEmailAddressEmail
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

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
    ) = if (passwordMatchesHash(currentPassword, user.password)) {
        user.password = passwordEncoder().encode(newPassword)
        userDAO.save(user)
    } else {
        throw WrongCurrentPasswordException()
    }


    override fun changeUserEmail(user: User, email: String): ChangePasswordRequest {
        val request = changePasswordRequestDAO.save(ChangePasswordRequest(
            token       = VerificationToken(user = user),
            newEmail    = email
        ))

        emailService.sendEmail(ConfirmNewEmailAddressEmail(request))

        return request
    }

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