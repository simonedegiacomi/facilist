package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import java.util.*
import javax.persistence.*

/**
 * Push notification subscription of a user device. A user can have different push subscriptions, because the informations
 * to send the notifications are different for each device..
 * The information stored in this entity are those needed to send a push notification using the Push API.
 *
 * You can find more info about the Push API here: https://developer.mozilla.org/en-US/docs/Web/API/Push_API
 */
@Entity
@Table(name = "push_subscription")
data class PushSubscription(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne()
        @JoinColumn(name = "user_id")
        val user: User,

        /**
         * Url to which send the notification
         */
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