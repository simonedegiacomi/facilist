package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "chat_subscription")
data class ChatMessage(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne()
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val user: User,

        @ManyToOne()
        @JoinColumn(name = "shopping_list_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JsonIgnore
        val shoppingList: ShoppingList,

        @Column
        @Lob
        val message: String,

        @Column
        val sentAt: Date = Date()
)