package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListCollaborationNotification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NotificationDAO {

    fun allOrderBySentAt(pageable: Pageable): Page<Notification>

    fun saveAll(notifications: List<ShoppingListCollaborationNotification>)

}