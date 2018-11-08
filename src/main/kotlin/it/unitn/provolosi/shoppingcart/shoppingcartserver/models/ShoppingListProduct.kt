package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
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
        @JsonIgnore
        val shoppingList: ShoppingList,

        @ManyToOne
        @JoinColumn(name = "product_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val product: Product,

        @Column
        var bought: Boolean = true,

        @Column
        var quantity: Int = 1,

        @Column
        var note: String? = null,


        @Column
        var image: String,


        @ManyToOne
        @JoinColumn(name = "recent_shopping_list_products_update_id")
        var recentShoppingListProductsUpdate: RecentShoppingListProductsUpdate?

) {
    init {
        shoppingList.products.add(this)
    }
}