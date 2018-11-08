package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "recent_shopping_list_products_update")
data class RecentShoppingListProductsUpdate (

        @ManyToOne()
    @JoinColumn(name = "shopping_list_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Id
    val shoppingList: ShoppingList,

        @Column
    val lastEditAt: Date = Date(),

    /*
    * NOTE: 'updateProducts' should hold the relation, but to make things easier let's make the product  to hold the relation.
    * Same thing for 'users'.
    */


    @OneToMany(
        mappedBy = "recentShoppingListProductsUpdate",
        cascade = [CascadeType.PERSIST]
    )
    private val updatedProducts: MutableSet<ShoppingListProduct> = mutableSetOf(),


        @OneToMany(
        mappedBy = "recentShoppingListProductsUpdate",
        cascade = [CascadeType.PERSIST]
    )
    private val users: MutableSet<ShoppingListCollaboration> = mutableSetOf()

) {
    fun addProduct (product: ShoppingListProduct) {
        product.recentShoppingListProductsUpdate = this
        updatedProducts.add(product)
    }

    fun addUser(user: ShoppingListCollaboration) {
        users.add(user)
        user.recentShoppingListProductsUpdate = this
    }
}