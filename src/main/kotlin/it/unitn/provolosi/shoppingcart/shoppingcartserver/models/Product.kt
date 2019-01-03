package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column()
        var name: String,

        @Column
        var icon: String,

        /**
         * When the creator is null the products is created by an admin and belongs to the system
         */
        @ManyToOne
        @JoinColumn(name = "creator_id")
        var creator: User? = null,


        @ManyToOne()
        @JoinColumn(name = "product_category_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val category: ProductCategory
) {

    /**
     * Utility method to check if the product was created by an admin
     */
    fun wasCreatedByAnAdmin() = creator == null

    /**
     * Checks if the product was created by the specified user
     */
    fun wasCreatedBy(user: User) = creator == user

    /**
     * Checks if the speicified user can edit the product.
     * A product can by modified only by:
     * - an admin if the product belongs to the system;
     * - the user who created the product if the product was created by a user
     */
    fun canBeEditedBy(user: User) = (user.isAdmin() && wasCreatedByAnAdmin()) || wasCreatedBy(user)
}
