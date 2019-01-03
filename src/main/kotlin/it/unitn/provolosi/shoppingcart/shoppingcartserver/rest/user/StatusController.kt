package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import ok
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class StatusController(
) {

    /**
     * Returns the user object. Useful to check if the user is logged in
     */
    @RequestMapping(
            value = ["/me"],
            method = [RequestMethod.GET]
    )
    fun status(@AppUser user: User) = ok(user)

}
