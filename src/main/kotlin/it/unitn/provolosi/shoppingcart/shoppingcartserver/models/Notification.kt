package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "notification")
data class Notification (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "shopping_list_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val shoppingList: ShoppingList,

        @OneToMany(
            cascade = [CascadeType.ALL]
        )
        val updates: MutableList<Update> = mutableListOf(),

        @ManyToOne
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val to: User,

        @Column
        var seenAt: Date? = null


)