package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.validation.Password
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/users")
class CompleteRecoverPasswordController(
        private val userDAO: UserDAO,
        private val tokenDAO: VerificationTokenDAO
) {

    @PostMapping("/completeRecoverPassword")
    @PreAuthorize("isAnonymous()")
    @ResponseStatus(HttpStatus.OK)
    fun completeRecoverPassword(
            @RequestBody @Valid recover: CompleteRecoverDTO
    ): ResponseEntity<Any> = try {

        val token = tokenDAO.findByToken(recover.token)

        val user = token.user
        if (user.email == recover.email) {
            user.password = BCryptPasswordEncoder().encode(recover.newPassword) // TODO: Refactor

            userDAO.save(user)
            tokenDAO.delete(token)

            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    } catch (ex: VerificationTokenNotFoundException) {
        ResponseEntity.notFound().build()
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