package it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "shopping_list_product_notification")
class ShoppingListProductNotification(

        shoppingList: ShoppingList,

        source: User,

        target: User,

        @OneToMany(
            mappedBy = "notification",
            cascade = [CascadeType.REMOVE]
        )
        val changes: MutableList<ShoppingListProductChange> = mutableListOf()
): Notification(
    shoppingList    = shoppingList,
    source          = source,
    target          = target,
    type            = TYPE
) {
    companion object {
        const val TYPE = "shopping_list_product_notification"
    }
}

@Entity
@Table(name = "shopping_list_product_notification__shopping_list_product")
data class ShoppingListProductChange (
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column
        val type: String,

        @ManyToOne()
        @JoinColumn(name = "shopping_list_product_notification_id")
        val notification: ShoppingListProductNotification,

        @ManyToOne()
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "shopping_list_product_id")
        val product: ShoppingListProduct
) {
    companion object {
        const val PRODUCT_ADDED     = "product_added"
        const val PRODUCT_REMOVED   = "product_removed"
        const val PRODUCT_UPDATE    = "product_updated" //   Quantity, image or note

        const val PRODUCT_BOUGHT    = "product_bought"
        const val PRODUCT_TO_BUY    = "product_to_buy"

    }
}