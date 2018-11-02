package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NotificationDAO {

    fun allByUserOrderBySentAt(user: User, pageable: Pageable): Page<Notification>

    fun saveAll(notifications: List<Notification>)

    fun save(notification: Notification): Notification

}