package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "shopping_list__product")
data class ShoppingListProduct(

        @Id
        @GeneratedValue
        val id: Long? = null,


        @ManyToOne
        @JoinColumn(name = "shopping_list_id")
        val shoppingList: ShoppingList,

        @ManyToOne
        @JoinColumn(name = "product_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val product: Product,

        @Column
        var toBuy: Boolean = true,

        @Column
        var quantity: Int = 1

) {
    init {
        shoppingList.products.add(this)
    }
}