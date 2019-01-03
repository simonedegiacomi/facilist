package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangeEmailRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotEmpty

class WrongCurrentPasswordException: Exception()

/**
 * Encapsulate complex actions applied to the user entity
 */
interface IUserService {

    /**
     * PAssword encoder to use to encode the passwords of the users
     */
    @Bean("passwordEncoder")
    fun passwordEncoder(): PasswordEncoder

    /**
     * Returs the hashed version of the specified string
     */
    fun hashPassword(plain: String): String = passwordEncoder().encode(plain)

    /**
     * Checks if the plain specified string is the same as the string that generated the specified hash.
     */
    fun passwordMatchesHash(plain: String, hash: String) = passwordEncoder().matches(plain, hash)


    fun registerUser(registration: RegisterDTO): User

    fun confirmUserRegistration(email: String, tokenString: String): User


    fun changeUserPassword(user: User, currentPassword: String, newPassword: String): User

    fun changeUserEmail(user: User, email: String): ChangeEmailRequest

    fun confirmChangeUserEmail(email: String, tokenString: String): User
}

/**
 * Object used to register a new user, with relative annotations to permit validation
 */
data class RegisterDTO(

        @get:javax.validation.constraints.Email
        val email: String,

        @get:NotEmpty
        val locale: String,

        @get:NotEmpty
        val firstName: String,

        @get:NotEmpty
        val lastName: String,

        @get:Password
        val password: String) {

    /**
     * Converts the DTO to the entity object that we need to insert into the database
     */
    fun toUser() = User(
        email       = email,
        firstName   = firstName,
        lastName    = lastName,
        password    = password
    )
}