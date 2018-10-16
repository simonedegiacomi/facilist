package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory.Companion.PRODUCT_CATEGORY_UNIQUE_NAME_CONSTRAINT
import javax.persistence.*



@Entity
@Table(
    name = "product_category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"], name = PRODUCT_CATEGORY_UNIQUE_NAME_CONSTRAINT)]
)
data class ProductCategory(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column()
        var name: String,

        @Column
        @Lob
        var description: String,

        @Column
        var icon: String = "default-product-category-icon"
) {
    companion object {
        const val PRODUCT_CATEGORY_UNIQUE_NAME_CONSTRAINT = "product_category_unique_name_constraint"
    }
}
