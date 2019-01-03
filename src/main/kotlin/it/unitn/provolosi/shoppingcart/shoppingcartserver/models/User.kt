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

        /**
         * When the user registers this field is set to false. The user won't be able to login until he verifies his email
         * address. Then this field will be set to true and the user will be able to login
         */
        @Column(name = "enabled")
        var emailVerified: Boolean = false,

        @Column
        var role: String = User.USER,

        @Column()
        @JsonIgnore()
        var password: String,

        /**
         * We need to track the user locale because, when we generate emails or notification on the server outside the
         * context of an HTTP request we can't use the headers sent in the request to identify the user locale.
         */
        @Column()
        var locale: String = DEFAULT_LOCALE,

        /**
         * Products created by the user. This list is used only if the user has the USER role.
         * An admin, even if he has created thousand of products, will have this list empty.
         */
        @OneToMany(
            mappedBy    = "creator",
            cascade     = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val products: List<Product> = mutableListOf(),

        /**
         * Shopping lists created (owned) by the user. This list doesn't contain the lists in which the user collaborates
         */
        @OneToMany(
            mappedBy    = "creator",
            cascade     = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val shoppingLists: List<ShoppingList> = mutableListOf(),

        /**
         * Collaborations in lists created (owned) by other users).
         */
        @OneToMany(
            mappedBy = "user",
            cascade = [CascadeType.REMOVE]
        )
        @JsonIgnore
        val collaborations: MutableList<ShoppingListCollaboration> = mutableListOf(),

        @Column
        var photo: String = "default-user-photo",

        /**
         * Push API subscriptions. This is a list because the user may have more devices.
         */
        @OneToMany(
            mappedBy = "user",
            cascade  = [CascadeType.ALL]
        )
        @JsonIgnore
        val pushSubscriptions: MutableList<PushSubscription> = mutableListOf(),

        /**
         * List of recent edits in lists grouped together.
         * See more in the ShoppingListProductUpdatesGroup documentation
         */
        @ManyToMany(
            mappedBy = "users",
            cascade     = [CascadeType.REMOVE] // When the user is deleted, remove the user from the update group
        )
        @JsonIgnore
        val recentShoppingListProductUpdatesGroup: MutableList<ShoppingListProductUpdatesGroup> = mutableListOf()

) {
    companion object {
        const val USER = "ROLE_USER"
        const val ADMIN = "ROLE_ADMIN"

        const val DEFAULT_LOCALE = "it-IT"

        const val USER_EMAIL_UNIQUE_NAME_CONSTRAINT = "user_email_unique_name_constraint"
    }

    @Transient()
    fun isAdmin () = role == ADMIN

    override fun toString() = "User(id=$id, email='$email')"


}

