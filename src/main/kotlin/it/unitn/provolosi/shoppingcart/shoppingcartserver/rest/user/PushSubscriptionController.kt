package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.PushSubscription
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/users/me/pushSubscriptions")
class PushSubscriptionController (
    private val userDAO: UserDAO
) {

    @PostMapping
    fun addSubscription(
            @AppUser user: User,
            @RequestBody @Valid add: AddSubscriptionDTO
    ): ResponseEntity<Any> {
        val oldSubscription = user.pushSubscriptions.find { s -> s.endpoint == add.endpoint }

        if (oldSubscription == null) {
            user.pushSubscriptions.add(add.toSubscription(user))
            userDAO.save(user)
        }

        return ResponseEntity.ok().build()
    }


    data class AddSubscriptionDTO(

            @get:NotNull
            @get:NotEmpty
            val endpoint: String?,

            @get:NotNull
            @get:NotEmpty
            val base64PublicKey: String?,

            @get:NotNull
            @get:NotEmpty
            val base64Auth: String?
    ) {
        fun toSubscription(user: User) = PushSubscription(
            user            = user,
            endpoint        = endpoint!!,
            base64PublicKey = base64PublicKey!!,
            base64Auth      = base64Auth!!
        )
    }
}