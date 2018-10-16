package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity()
@Table(name = "user")
data class User(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Column(unique = true)
        val email: String,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "enabled")
        var emailVerified: Boolean = false,

        @Column
        var role: String,

        @Column()
        @JsonIgnore()
        var password: String,

        @OneToMany(
            mappedBy = "creator",
            cascade = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val products: List<Product> = mutableListOf()

) {
    companion object {
        const val USER = "ROLE_USER"
        const val ADMIN = "ROLE_ADMIN"
    }

    @Transient()
    fun isAdmin () = role == ADMIN

}

