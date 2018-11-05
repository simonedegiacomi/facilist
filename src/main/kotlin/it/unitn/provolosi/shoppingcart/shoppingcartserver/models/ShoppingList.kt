package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
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
        val invites: MutableList<InviteToJoin> = mutableListOf(),

        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val products:MutableList<ShoppingListProduct> = mutableListOf()
) {
    fun isShared() = collaborations.isNotEmpty()

    fun isUserOwnerOrCollaborator(user: User) = creator == user || collaborations.any { c -> c.user == user }

    fun canUserEditCollaborations(user: User) = creator == user || collaborations.any { c -> c.user == user && c.canEditCollaborations() }

    fun canUserEditList(user: User) = creator == user || collaborations.any { c -> c.user == user && c.canEditList() }

    fun ownerAndCollaborators() = listOf(creator, *collaborations.map { c -> c.user }.toTypedArray())

    fun toPreview() = ShoppingListPreview(
        shoppingList        = this,
        id                  = this.id!!,
        name                = this.name,
        description         = this.description,
        icon                = this.icon,

        boughtItemsCount    = this.products.count { p -> p.bought },
        itemsCount          = this.products.size,

        isShared            = this.isShared()
    )
}

data class ShoppingListPreview(

        @JsonIgnore
        val shoppingList: ShoppingList,

        val id: Long,
        val name: String,
        val description: String?,
        val icon: String,

        val boughtItemsCount: Int,
        val itemsCount: Int,

        val isShared: Boolean
) {

    val itemsToBuy: Int
        get() = itemsCount - boughtItemsCount
}