package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface InternalSpringJPANotificationDAO: JpaRepository<Notification, Long> {}

@Component
class SpringJPANotificationDAO(
        private val springRepository: InternalSpringJPANotificationDAO
): NotificationDAO {

}