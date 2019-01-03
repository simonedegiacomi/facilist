package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users/me/notifications")
class MarkNotificationsAsReadController (
        private val notificationDAO: NotificationDAO
) {

    /**
     * Marks the notifications of the user as seen
     */
    @PostMapping("/lastSeen")
    fun markNotificationsAsRead (
            @AppUser user: User,
            @RequestBody @Valid date: Date
    ) {
        notificationDAO.markUnreadNotificationsOfUserBeforeDateAsRead(user, date)
    }

}