package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class SearchByNameController (
        private val userDAO: UserDAO
) {

    @GetMapping
    fun searchByName(
            @RequestParam(name = "email", defaultValue = "") email: String
    ): ResponseEntity<List<UserPreview>> = ResponseEntity.ok(
        userDAO.findUserByEmailContainingIgnoreCaseLimit10(email).map { user -> UserPreview(user) }
    )

}

class UserPreview(
        user: User,

        val email: String = user.email,
        val photo: String = user.photo
)