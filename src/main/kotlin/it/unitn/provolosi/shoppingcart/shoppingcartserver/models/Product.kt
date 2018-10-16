package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*
import kotlin.jvm.Transient

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


        @ManyToOne
        @JoinColumn(name = "creator_id")
        var creator: User? = null,


        @ManyToOne()
        @JoinColumn(name = "product_category_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val category: ProductCategory
) {


    fun wasCreatedByAnAdmin() = creator == null

    fun wasCreatedBy(user: User) = creator == user
}