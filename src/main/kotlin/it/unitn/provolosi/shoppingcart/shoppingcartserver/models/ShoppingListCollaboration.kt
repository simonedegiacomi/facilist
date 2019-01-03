package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration.Companion.SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT
import javax.persistence.*

/**
 * This entity describes the collaboration of a user in a list
 */
@Entity
@Table(
    name = "shopping_list__user",
    uniqueConstraints = [UniqueConstraint(
        columnNames = ["user_id", "shopping_list_id"],
        name = SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT
    )]
)
data class ShoppingListCollaboration (

        @Id
        @GeneratedValue
        val id: Long? = null,

        /**
         * The role of the user in the list
         */
        @Column
        var role: String = BASIC,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @ManyToOne
        @JoinColumn(name = "shopping_list_id")
        @JsonIgnore
        val shoppingList: ShoppingList,

        /**
         * Pending group of edits. This field is not null when some of the edits are made by this collaborator
         */
        @ManyToOne
        @JoinColumn(name = "recent_shopping_list_products_update_id")
        @JsonIgnore
        var shoppingListProductUpdatesGroup: ShoppingListProductUpdatesGroup? = null
) {

    companion object {
        /**
         * User can add, edit and remove products of the list
         */
        const val BASIC     = "BASIC"

        /**
         * Same as BASIC but can also add, change role or remove collaborators
         */
        const val SOCIAL    = "SOCIAL"

        /**
         * Same as SOCIAL, but can also change the list name, descriptio and icon.
         */
        const val ADMIN     = "ADMIN"

        const val SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT = "shopping_list_collaboration_unique_constraint"

        /**
         * Checks if a string describes a role
         */
        fun isRoleValid (role: String) = role == BASIC ||
                        role == SOCIAL ||
                        role == ADMIN
    }

    init {
        shoppingList.collaborations.add(this)
        user.collaborations.add(this)
    }

    fun canEditCollaborations() = role == SOCIAL || role == ADMIN

    fun canEditList() = role == ADMIN

    override fun toString() =  "ShoppingListCollaboration(id=$id, user=$user)"


}
