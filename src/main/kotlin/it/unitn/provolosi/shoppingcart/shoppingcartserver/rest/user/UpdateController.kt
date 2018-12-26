package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/users/me")
class UpdateController(
        private val userDAO: UserDAO
) {

    /**
     * Handler of the request to change the user image or locale
     */
    @PutMapping
    fun update(
            @AppUser user: User,
            @Valid @RequestBody update: UserUpdateDTO
    ) {
        user.photo  = update.photo!!
        user.locale = update.locale!!
        userDAO.save(user)
    }


    data class UserUpdateDTO(
            @get:NotEmpty
            val locale: String?,

            @get:NotEmpty
            val photo: String?
    )
}