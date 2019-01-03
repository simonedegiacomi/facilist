package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

/**
 * Model of the pending request to change a user password.
 */
@Entity
@Table
data class ChangePasswordRequest (

        @Id
        @GeneratedValue
        val id: Long? = null,

        /**
         * The token sent as email. In the token there is also a reference to the user that request the email change
         */
        @OneToOne(
                cascade = [CascadeType.ALL]
        )
        @JoinColumn(name = "verification_token_id")
        val token: VerificationToken,

        /**
         * The new mail that the user want to set
         */
        @Column
        val newEmail: String
)