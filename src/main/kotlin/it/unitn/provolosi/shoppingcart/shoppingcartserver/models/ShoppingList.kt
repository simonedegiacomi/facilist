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

        /**
         * The owner of the list, who also created the list
         */
        @ManyToOne
        @JoinColumn(name = "creator_id")
        val creator: User,

        /**
         * List of collaborations. Each collaboration contains a reference to the collaborating user and his role in the list
         */
        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val collaborations: MutableList<ShoppingListCollaboration> = mutableListOf(),

        /**
         * List of invited sent to unregistered people
         */
        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val invites: MutableList<InviteToJoin> = mutableListOf(),

        /**
         * List of objects with reference to products and related information about a product in this list.
         */
        @OneToMany(
            mappedBy = "shoppingList",
            cascade = [CascadeType.REMOVE]
        )
        val products:MutableList<ShoppingListProduct> = mutableListOf(),

        /**
         * When users edit products in the list, edits are grouped toghether to, later on, create a unique notification.
         * This is the reference to the group of edits which notification is not sent yet. When the notification will be
         * sent, this field will be set to null. It will remain null until another user updated the list.
         */
        @OneToOne(
            mappedBy = "shoppingList"
        )
        @JsonIgnore
        var productUpdatesGroup: ShoppingListProductUpdatesGroup? = null
) {
    fun isShared() = collaborations.isNotEmpty()

    fun isUserOwnerOrCollaborator(user: User) = creator == user || collaborations.any { c -> c.user == user }

    /**
     * Checks if the specified user can update the collaborators list.
     */
    fun canUserEditCollaborations(user: User) = creator == user || collaborations.any { c -> c.user == user && c.canEditCollaborations() }

    /**
     * Checks if a user can edit the list informations (photo, name and description)
     */
    fun canUserEditList(user: User) = creator == user || collaborations.any { c -> c.user == user && c.canEditList() }

    /**
     * Returns a list of users, the collaborators and the owner.
     */
    fun ownerAndCollaborators() = listOf(creator, *collaborations.map { c -> c.user }.toTypedArray())

    /**
     * Return an object with flatted data of the list.
     */
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

    override fun toString() = "ShoppingList(id=$id, name='$name')"

    /**
     * Return the collaboration object that describe the collaboration of a user (ex: his role).
     * If the user is not collaborating an exception will be thrown.
     */
    fun getCollaborationOfUser(user: User): ShoppingListCollaboration = collaborations.find { it.user.id == user.id }!!

}

/**
 * The flatted data representation of the list
 */
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