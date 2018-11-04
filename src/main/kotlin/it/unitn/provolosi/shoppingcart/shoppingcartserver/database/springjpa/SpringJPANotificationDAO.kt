package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.NotificationDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
interface InternalSpringJPANotificationDAO : JpaRepository<Notification, Long> {

    fun findAllByTargetOrderBySentAtDesc(target: User, pageable: Pageable): Page<Notification>

    @Query("UPDATE Notification n SET n.seenAt = :date WHERE n.target = :user AND n.seenAt IS NULL AND n.sentAt <= :date")
    @Modifying
    @Transactional
    fun markUnreadNotificationsOfUserBeforeDateAsRead(user: User, date: Date)

}

@Component
class SpringJPANotificationDAO(
        private val springRepository: InternalSpringJPANotificationDAO
) : NotificationDAO {
    override fun save(notification: Notification) = springRepository.save(notification)

    override fun saveAll(notifications: List<Notification>) {
        springRepository.saveAll(notifications)
    }

    override fun allByUserOrderBySentAt(user: User, pageable: Pageable) =
            springRepository.findAllByTargetOrderBySentAtDesc(user, pageable)

    override fun markUnreadNotificationsOfUserBeforeDateAsRead(user: User, date: Date) =
            springRepository.markUnreadNotificationsOfUserBeforeDateAsRead(user, date)
}