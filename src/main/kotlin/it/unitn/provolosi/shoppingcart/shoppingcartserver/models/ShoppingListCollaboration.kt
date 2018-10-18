package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

@Entity
@Table(name = "shopping_list__user")
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
        val shoppingList: ShoppingList
) {
    companion object {
        const val BASIC     = "BASIC"
        const val SOCIAL    = "SOCIAL"
        const val ADMIN     = "ADMIN"
    }

    init {
        shoppingList.collaborations.add(this)
        user.collaborations.add(this)
    }
}
