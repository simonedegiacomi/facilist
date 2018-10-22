package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

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