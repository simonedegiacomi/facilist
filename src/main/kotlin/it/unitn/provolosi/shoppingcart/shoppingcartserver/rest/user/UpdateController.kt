package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import forbidden
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.IUserService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.WrongCurrentPasswordException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

/**
 * Controller that handle the update of the user resource
 */
@RestController
@RequestMapping("/api/users/me")
class UpdateController(
        private val userDAO: UserDAO,
        private val userService: IUserService
) {

    /**
     * Handler of the request to change the user photo or locale
     */
    @PutMapping
    fun update(
            @AppUser user: User,
            @Valid @RequestBody update: UserUpdateDTO
    ) {
        user.photo = update.photo!!
        user.locale = update.locale!!
        userDAO.save(user)
    }


    data class UserUpdateDTO(
            @get:NotEmpty
            val locale: String?,

            @get:NotEmpty
            val photo: String?
    )


    /**
     * Handler of the request to change the user password
     */
    @PutMapping("/password")
    fun changePassword(
            @AppUser user: User,
            @Valid @RequestBody update: UpdatePasswordDTO
    ): ResponseEntity<Any> = try {
        userService.changeUserPassword(user, update.currentPassword!!, update.newPassword!!)
        ok()
    } catch (ex: WrongCurrentPasswordException) {
        forbidden()
    }

    data class UpdatePasswordDTO(
            @get:NotEmpty
            val currentPassword: String?,

            @get:Password
            val newPassword: String?
    )


    /**
     * Handler of the request to change the user email.
     * The process to update the user email consist in two requests: This is the first one and the other is the
     * request sent after that the user has clicked on the link sent by email
     */
    @PutMapping("/email")
    fun changeEmail(
            @AppUser user: User,
            @Valid @RequestBody @Email newEmail: String
    ): ResponseEntity<Any> {
        userService.changeUserEmail(user, newEmail)
        return ok()
    }

    /**
     * Handler of the second request to confirm the email change.
     */
    @PostMapping("/confirmEmailChange/{email}")
    fun confirmEmailChange(
            @PathVariable email: String,
            @RequestParam("token") tokenString: String
    ): ResponseEntity<Any> = try {
        userService.confirmChangeUserEmail(email, tokenString)
        ok()
    } catch (ex: ChangePasswordRequestNotFoundException) {
        notFound()
    }
}
