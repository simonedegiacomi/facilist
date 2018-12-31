package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import conflict
import created
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.EmailAlreadyInUseException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.IUserService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.RegisterDTO
import notFound
import ok
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class RegistrationController (
        val userDAO: UserDAO,
        val userService: IUserService
) {

    @PostMapping("/register")
    fun registerUser(
            @RequestBody @Valid registration: RegisterDTO,
            req: HttpServletRequest
    ): ResponseEntity<User> = try {
        val user = userService.registerUser(registration)
        created(user)
    } catch (ex: EmailAlreadyInUseException) {
        conflict()
    }



    @PostMapping("/verifyEmail/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun confirmRegistration(
            @PathVariable email: String,
            @RequestParam("token") tokenString: String
    ): ResponseEntity<Any> = try {
        userService.confirmUserRegistration(email, tokenString)

       ok()
    } catch (ex: VerificationTokenNotFoundException) {
        notFound()
    }
}
