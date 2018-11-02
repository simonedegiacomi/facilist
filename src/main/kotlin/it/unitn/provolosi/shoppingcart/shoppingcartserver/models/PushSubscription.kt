package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "push_subscription")
data class PushSubscription(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne()
        @JoinColumn(name = "user_id")
        val user: User,

        @Column
        val endpoint: String,

        @Column
        val base64PublicKey: String,

        @Column
        val base64Auth: String

) {
        val authAsBytes: ByteArray
                get() = Base64.getDecoder().decode(base64Auth)

        val publicKeyAsBytes: ByteArray
            get() = Base64.getDecoder().decode(base64PublicKey)
}