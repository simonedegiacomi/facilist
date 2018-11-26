package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import com.fasterxml.jackson.annotation.JsonIgnore
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
        var icon: String = "default-product-category-icon",

        @ManyToMany(
            mappedBy    = "productCategories",
            cascade     = [CascadeType.REMOVE] // When the category is deleted, remove the category from the shopping list categories
        )
        @JsonIgnore
        private var shoppingLists: List<ShoppingListCategory> = mutableListOf()
) {
    companion object {
        const val PRODUCT_CATEGORY_UNIQUE_NAME_CONSTRAINT = "product_category_unique_name_constraint"
    }

    override fun toString() = "ProductCategory(name='$name')"


}
