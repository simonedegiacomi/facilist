package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.EmailService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.ConfirmAccountEmail
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails.ConfirmNewEmailAddressEmail
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserService(
        private val userDAO: UserDAO,
        private val changePasswordRequestDAO: ChangePasswordRequestDAO,
        private val emailService: EmailService,
        private val tokenDAO: VerificationTokenDAO,
        private val inviteToJoinDAO: InviteToJoinDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO
) : IUserService {

    @Bean("passwordEncoder")
    override fun passwordEncoder() = BCryptPasswordEncoder()

    @Throws(EmailAlreadyInUseException::class)
    override fun registerUser(registration: RegisterDTO): User {
        val user = userDAO.save(registration.toUser())

        val token = tokenDAO.save(VerificationToken(user = user))

        emailService.sendEmail(ConfirmAccountEmail(token))

        return user
    }

    @Throws(VerificationTokenNotFoundException::class)
    override fun confirmUserRegistration(email: String, tokenString: String):User {

        val token = tokenDAO.findByToken(tokenString)
        val user = token.user


        if (user.email != email) {
            throw VerificationTokenNotFoundException()
        } else if(!user.emailVerified) {
            user.emailVerified = true

            userDAO.save(user)
            tokenDAO.delete(token)
            acceptInvitesForUser(user)
        }

        return user
    }

    private fun acceptInvitesForUser(user: User) = inviteToJoinDAO
            .findByEmail(user.email)
            .forEach { invite ->
                shoppingListCollaborationDAO.save(ShoppingListCollaboration(
                    user            = user,
                    shoppingList    = invite.shoppingList
                ))
            }


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