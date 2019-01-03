package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

/**
 * This entity is used to group edits to shared shopping list to then create a single notification to be sent to all the
 * collaborators.
 */
@Entity
@Table(name = "shopping_list_product_updates_group")
data class ShoppingListProductUpdatesGroup(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @OneToOne()
        @JoinColumn(
            name = "shopping_list_id"
        )
        @OnDelete(action = OnDeleteAction.CASCADE)
        val shoppingList: ShoppingList,

        @Column
        var lastEditAt: Date = Date(),

    /*
    * NOTE: 'updateProducts' should hold the relation, but to make things easier let's make the product to hold the relation.
    * Same thing for 'users'.
    */


        /**
         * Added, updated or removed products.
         * In the notification we'll not take into account the type of edit, so there isn't need to store that
         */
        @OneToMany(
            mappedBy = "shoppingListProductUpdatesGroup",
            cascade = [CascadeType.PERSIST]
        )
        val updatedProducts: MutableSet<ShoppingListProduct> = mutableSetOf(),


        /**
         * List of users that added, edited or removed products
         */
        @ManyToMany()
        @JoinTable(
            name = "shopping_list_product_updates_group__user",
            joinColumns = [JoinColumn(name = "shopping_list_product_groups_id")],
            inverseJoinColumns = [JoinColumn(name = "user_id")]
        )
        val users: MutableSet<User> = mutableSetOf()

) {

    fun addProduct(product: ShoppingListProduct) {
        product.shoppingListProductUpdatesGroup = this
        updatedProducts.add(product)
    }

    fun addUser(user: User) {
        users.add(user)
    }
}