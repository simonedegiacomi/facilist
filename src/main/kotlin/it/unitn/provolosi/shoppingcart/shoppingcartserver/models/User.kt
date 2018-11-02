package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User.Companion.USER_EMAIL_UNIQUE_NAME_CONSTRAINT
import javax.persistence.*

@Entity()
@Table(
    name = "user",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"], name = USER_EMAIL_UNIQUE_NAME_CONSTRAINT)]
)
data class User(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Column()
        var email: String,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "enabled")
        var emailVerified: Boolean = false,

        @Column
        var role: String = User.USER,

        @Column()
        @JsonIgnore()
        var password: String,

        @OneToMany(
            mappedBy    = "creator",
            cascade     = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val products: List<Product> = mutableListOf(),

        @OneToMany(
            mappedBy    = "creator",
            cascade     = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val shoppingLists: List<ShoppingList> = mutableListOf(),

        @OneToMany(
            mappedBy = "user",
            cascade = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val collaborations: MutableList<ShoppingListCollaboration> = mutableListOf(),

        @Column
        var photo: String = "default-user-photo",

        @OneToMany(
            mappedBy = "user",
            cascade  = [CascadeType.REMOVE]
        )
        val pushSubscriptions: MutableList<PushSubscription> = mutableListOf()

) {
    companion object {
        const val USER = "ROLE_USER"
        const val ADMIN = "ROLE_ADMIN"

        const val USER_EMAIL_UNIQUE_NAME_CONSTRAINT = "user_email_unique_name_constraint"
    }

    @Transient()
    fun isAdmin () = role == ADMIN

}

