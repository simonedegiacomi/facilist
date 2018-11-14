package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct.Companion.SHOPPING_LIST_PRODUCT_UNIQUE_CONSTRAINT
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(
    name = "shopping_list__product",
    uniqueConstraints = [UniqueConstraint(
        columnNames = ["product_id", "shopping_list_id"],
        name = SHOPPING_LIST_PRODUCT_UNIQUE_CONSTRAINT
    )]
)
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


        @OneToOne
        @JoinColumn(name = "shopping_list_product_updates_group_id")
        @JsonIgnore
        var shoppingListProductUpdatesGroup: ShoppingListProductUpdatesGroup? = null

) {

    companion object {
        const val SHOPPING_LIST_PRODUCT_UNIQUE_CONSTRAINT = "shopping_list_product_unique_constraint"
    }


    init {
        shoppingList.products.add(this)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        if (id != null) {
            return id.toInt()
        }
        return 0
    }
}