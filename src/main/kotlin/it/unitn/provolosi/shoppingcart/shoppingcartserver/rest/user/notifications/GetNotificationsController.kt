package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.DEFAULT_PAGE_SIZE_PARAM
import ok
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/me/notifications")
class GetNotificationsController(
        private val notificationDAO: NotificationDAO
) {

    /**
     * Returns the notifications of the user
     */
    @GetMapping
    fun getNotificationsInOrder(
            @RequestParam(name = "page", defaultValue = "0") page: Int,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_PARAM) size: Int,
            @AppUser user: User
    ) = ok(notificationDAO.allByUserOrderBySentAt(user, PageRequest.of(page, size)))

}
