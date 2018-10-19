package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "shopping_list")
data class ShoppingList(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column()
        var name: String,

        @ManyToOne()
        @JoinColumn(name = "shopping_list_category_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val category: ShoppingListCategory,

        @Column
        @Lob
        var description: String?,

        @Column
        var icon: String = "default-shopping-list-icon",

        @ManyToOne
        @JoinColumn(name = "creator_id")
        val creator: User,

        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val collaborations: MutableList<ShoppingListCollaboration> = mutableListOf(),

        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val products:MutableList<ShoppingListProduct> = mutableListOf()
) {
    fun isShared() = collaborations.isNotEmpty()

    fun isUserOwnerOrCollaborator(user: User) = creator == user || collaborations.any { c -> c.user == user }
}