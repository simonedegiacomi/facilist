package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

/**
 * Base entity to store a token that it's used to verify the user email address
 */
@Table(name = "verification_token")
@Entity
data class VerificationToken(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column
        val token: String = generateToken(),

        @ManyToOne()
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val user: User,

        @Column(name = "creation_time")
        val creationTime: Date = Date()

) {

    companion object {
        private fun generateToken() = UUID.randomUUID().toString()
    }

}