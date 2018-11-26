package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder

class WrongCurrentPasswordException: Exception()

interface IUserService {

    @Bean("passwordEncoder")
    fun passwordEncoder(): PasswordEncoder



    fun changeUserPassword (user: User, currentPassword: String, newPassword: String): User

    fun changeUserEmail(user: User, email: String): ChangePasswordRequest

    fun confirmChangeUserEmail(email: String, tokenString: String): User
}