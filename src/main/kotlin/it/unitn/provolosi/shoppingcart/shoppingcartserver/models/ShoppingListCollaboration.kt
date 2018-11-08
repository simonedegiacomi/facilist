package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration.Companion.SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT
import javax.persistence.*

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

        @Column
        var role: String = BASIC,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @ManyToOne
        @JoinColumn(name = "shopping_list_id")
        @JsonIgnore
        val shoppingList: ShoppingList,


        @ManyToOne
        @JoinColumn(name = "recent_shopping_list_products_update_id")
        var recentShoppingListProductsUpdate: RecentShoppingListProductsUpdate?
) {

    companion object {
        const val BASIC     = "BASIC"
        const val SOCIAL    = "SOCIAL"
        const val ADMIN     = "ADMIN"

        const val SHOPPING_LIST_COLLABORATION_UNIQUE_CONSTRAINT = "shopping_list_collaboration_unique_constraint"
    }

    init {
        shoppingList.collaborations.add(this)
        user.collaborations.add(this)
    }

    fun canEditCollaborations() = role == SOCIAL || role == ADMIN

    fun canEditList() = role == ADMIN
}
