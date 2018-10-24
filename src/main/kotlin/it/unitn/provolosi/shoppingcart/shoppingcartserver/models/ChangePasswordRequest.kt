package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

@Entity
@Table
data class ChangePasswordRequest (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @OneToOne(
                cascade = [CascadeType.ALL]
        )
        @JoinColumn(name = "verification_token_id")
        val token: VerificationToken,

        @Column
        val newEmail: String
)