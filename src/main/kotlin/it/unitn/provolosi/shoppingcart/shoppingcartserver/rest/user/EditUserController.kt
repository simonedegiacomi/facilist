package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.IUserService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.WrongCurrentPasswordException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/users")
class EditUserController(
        private val userDAO: UserDAO,
        private val userService: IUserService
) {

    @PostMapping("/me/password")
    fun changePassword(
            @AppUser user: User,
            @RequestBody @Valid update: ChangePasswordDTO
    ): ResponseEntity<Any> = try {
        this.userService.changeUserPassword(
            user,
            update.currentPassword!!,
            update.newPassword!!
        )
        ResponseEntity.ok().build()
    } catch (ex: WrongCurrentPasswordException) {
        ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    data class ChangePasswordDTO(

            @get:NotNull
            @get:NotEmpty
            val currentPassword: String?,

            @get:NotNull
            @get:NotEmpty
            val newPassword: String?
    )

    @PostMapping("/me/photo")
    fun changePhoto(
            @AppUser user: User,
            @RequestBody @NotNull @NotEmpty imageId: String
    ): ResponseEntity<User> {
        user.photo = imageId
        userDAO.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/me/email")
    fun changeEmail(
            @AppUser user: User,
            @RequestBody @NotNull @Email email: String,
            req: HttpServletRequest
    ): ResponseEntity<Any> {
        this.userService.changeUserEmail(user, email, req)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/confirmEmailChange/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun verifyToken(
            @PathVariable email: String,
            @RequestParam("token") tokenString: String
    ): ResponseEntity<Any> = try {

        userService.confirmChangeUserEmail(email, tokenString)
        ResponseEntity.ok().build()

    } catch (ex: ChangePasswordRequestNotFoundException) {

        ResponseEntity.notFound().build()
    }

}
