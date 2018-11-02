package it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "shopping_list_notification")
class ShoppingListNotification(

        shoppingList: ShoppingList,

        source: User,

        target: User,

        @Column
        val action: String

) : Notification(
    shoppingList    = shoppingList,
    source          = source,
    target          = target,
    type            = TYPE
) {
    companion object {
        const val TYPE = "shopping_list_notification"

        const val ACTION_UPDATE      = "action_update"
        const val ACTION_DELETE      = "action_delete"
    }
}
