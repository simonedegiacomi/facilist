package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications.ShoppingListCollaborationNotification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface InternalSpringJPANotificationDAO : JpaRepository<Notification, Long> {

    fun findAllByOrderBySentAtDesc(pageable: Pageable): Page<Notification>

}

@Component
class SpringJPANotificationDAO(
        private val springRepository: InternalSpringJPANotificationDAO
) : NotificationDAO {
    override fun saveAll(notifications: List<ShoppingListCollaborationNotification>) {
        springRepository.saveAll(notifications)
    }

    override fun allOrderBySentAt(pageable: Pageable) = springRepository.findAllByOrderBySentAtDesc(pageable)

}