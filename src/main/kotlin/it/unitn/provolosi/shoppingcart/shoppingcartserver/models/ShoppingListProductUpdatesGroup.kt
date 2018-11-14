package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "shopping_list_product_updates_group")
data class ShoppingListProductUpdatesGroup(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @OneToOne()
        @JoinColumn(name = "shopping_list_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val shoppingList: ShoppingList,

        @Column
        var lastEditAt: Date = Date(),

    /*
    * NOTE: 'updateProducts' should hold the relation, but to make things easier let's make the product  to hold the relation.
    * Same thing for '_users'.
    */


        @OneToMany(
            mappedBy = "shoppingListProductUpdatesGroup",
            cascade = [CascadeType.PERSIST]
        )
        val updatedProducts: MutableSet<ShoppingListProduct> = mutableSetOf(),


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