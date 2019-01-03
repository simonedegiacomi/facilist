package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import badRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.user.IUserService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import notFound
import ok
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/users")
class CompleteRecoverPasswordController(
        private val userDAO: UserDAO,
        private val tokenDAO: VerificationTokenDAO,
        private val userService: IUserService
) {

    /**
     * Handler of the request to complete the recover of the password (sent when the user clicked on the recover link)
     */
    @PostMapping("/completeRecoverPassword")
    @PreAuthorize("isAnonymous()")
    @ResponseStatus(HttpStatus.OK)
    fun completeRecoverPassword(
            @RequestBody @Valid recover: CompleteRecoverDTO
    ): ResponseEntity<Any> = try {

        val token = tokenDAO.findByToken(recover.token)

        val user = token.user
        if (user.email == recover.email) {
            user.password = userService.hashPassword(recover.newPassword)

            userDAO.save(user)
            tokenDAO.delete(token)

            ok()
        } else {
            badRequest()
        }
    } catch (ex: VerificationTokenNotFoundException) {
        notFound()
    }

    data class CompleteRecoverDTO(
            @get:Password
            val newPassword: String,

            @get:NotEmpty
            val token: String,

            @get:Email
            val email: String
    )
}