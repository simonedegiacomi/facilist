package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin.Companion.INVITE_TO_COLLABORATE__UNIQUE_INVITE_FOR_SHOPPING_LIST_PER_MAIL_CONSTRAINT
import javax.persistence.*

@Entity
@Table(
    name = "invite_to_collaborate",
    uniqueConstraints = [UniqueConstraint(
        columnNames = ["email", "shopping_list_id"],
        name = INVITE_TO_COLLABORATE__UNIQUE_INVITE_FOR_SHOPPING_LIST_PER_MAIL_CONSTRAINT
    )]
)
data class InviteToJoin(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column
        val email: String,

        @ManyToOne
        @JoinColumn(name = "shopping_list_id")
        @JsonIgnore
        val shoppingList: ShoppingList,

        @ManyToOne
        @JoinColumn(name = "inviter_id")
        @JsonIgnore
        val inviter: User
) {
    companion object {
        const val INVITE_TO_COLLABORATE__UNIQUE_INVITE_FOR_SHOPPING_LIST_PER_MAIL_CONSTRAINT = "invite_to_collaborate__unique_invite_for_shopping_list_per_mail_constraint"
    }
}