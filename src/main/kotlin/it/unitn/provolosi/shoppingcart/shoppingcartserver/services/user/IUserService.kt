package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotEmpty

class WrongCurrentPasswordException: Exception()

interface IUserService {

    @Bean("passwordEncoder")
    fun passwordEncoder(): PasswordEncoder

    fun hashPassword(plain: String): String = passwordEncoder().encode(plain)

    fun passwordMatchesHash(plain: String, hash: String) = passwordEncoder().matches(plain, hash)


    fun registerUser(registration: RegisterDTO): User

    fun confirmUserRegistration(email: String, tokenString: String): User


    fun changeUserPassword(user: User, currentPassword: String, newPassword: String): User

    fun changeUserEmail(user: User, email: String): ChangePasswordRequest

    fun confirmChangeUserEmail(email: String, tokenString: String): User
}

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

    fun toUser() = User(
        email       = email,
        firstName   = firstName,
        lastName    = lastName,
        password    = password
    )
}