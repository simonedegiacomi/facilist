package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "event")
data class Update (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "notification_id")
        @JsonIgnore
        val notification: Notification,

        @ManyToOne
        @JoinColumn(name = "generator_user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val generatedBy: User,

        @Column
        val generatedAt: Date = Date(),

        @Column
        val event: String

) {
    companion object {
        const val YOU_HAVE_BEEN_ADDED_TO_A_LIST                 = "you_have_been_added_to_a_list"
        const val NEW_COLLABORATOR_IN_LIST                      = "new_collaborator_in_list"
        const val YOU_HAVE_BEEN_REMOVED_FROM_A_LIST             = "you_have_been_removed_from_a_list"
        const val COLLABORATOR_REMOVED_FROM_LIST                = "collaborator_removed_from_list"
        const val YOUR_COLLABORATION_PERMISSIONS_HAVE_CHANGED   = "your_collaboration_permissions_have_changed"
        const val LIST_INFORMATIONS_UPDATED                     = "list_informations_updated"

        const val PRODUCT_ADDED                                 = "product_added"
        const val PRODUCT_REMOVED                               = "product_removed"
        const val PRODUCT_EDITED                                = "product_edited"

    }

    init {
        this.notification.updates.add(this)
    }
}