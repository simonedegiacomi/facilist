package it.unitn.provolosi.shoppingcart.shoppingcartserver.models.notifications

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "shopping_list_collaboration_notification")
class ShoppingListCollaborationNotification(

        shoppingList: ShoppingList,

        source: User,

        target: User,

        @Column
        val action: String,

        @ManyToOne
        @JoinColumn(name = "shopping_list_collaboration_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val collaboration: ShoppingListCollaboration

) : Notification(
    shoppingList    = shoppingList,
    source          = source,
    target          = target,
    type            = TYPE
) {

    companion object {
        const val TYPE = "shopping_list_collaboration_notification"

        const val ACTION_ADD_COLLABORATOR       = "action_add_collaborator"
        const val ACTION_EDIT_COLLABORATOR      = "action_edit_collaborator"
        const val ACTION_REMOVE_COLLABORATOR    = "action_remove_collaborator"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShoppingListCollaborationNotification) return false
        if (!super.equals(other)) return false

        if (action != other.action) return false

        return true
    }

    override fun hashCode() = super.hashCode()


}
