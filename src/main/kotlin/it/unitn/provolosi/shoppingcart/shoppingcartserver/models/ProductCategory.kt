package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

@Entity
@Table(name = "product_category")
data class ProductCategory(
        
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column(unique = true)
        var name: String,

        @Column
        @Lob
        var description: String,

        @Column
        var icon: String = "default-product-category-icon"
)
