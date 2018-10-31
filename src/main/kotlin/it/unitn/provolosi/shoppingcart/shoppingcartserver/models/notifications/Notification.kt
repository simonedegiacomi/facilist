package it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Inheritance(strategy = InheritanceType.JOINED)
@Entity()
@Table(name = "notification")
abstract class Notification(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column
        val type: String,

        @ManyToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "shopping_list_id")
        val shoppingList: ShoppingList,

        @Column
        val sentAt: Date = Date(),

        @Column(nullable = true)
        val seenAt: Date? = null,

        @ManyToOne()
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "source_user_id")
        val source: User,


        @ManyToOne()
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "target_user_id")
        val target: User

) {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Notification) return false

                if (id != other.id) return false
                if (type != other.type) return false
                if (sentAt != other.sentAt) return false
                if (seenAt != other.seenAt) return false
                if (source != other.source) return false
                if (target != other.target) return false

                return true
        }

        override fun hashCode() = id?.hashCode() ?: 0
}
