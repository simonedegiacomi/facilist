package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Inheritance(strategy = InheritanceType.JOINED)
@Entity()
@Table(name = "notification")
data class Notification(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column
        val message: String,

        val icon: String,

        @Column
        val sentAt: Date = Date(),

        @Column(nullable = true)
        val seenAt: Date? = null,

        @ManyToOne()
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "target_user_id")
        @JsonIgnore
        val target: User

)