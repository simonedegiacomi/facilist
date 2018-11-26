package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

@Entity()
@Table(
    name = "shopping_list_category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"], name = ShoppingListCategory.SHOPPING_LIST_CATEGORY_UNIQUE_NAME_CONSTRAINT)]
)
data class ShoppingListCategory(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column()
        var name: String,

        @Column
        var icon: String = "default-shopping-list-category-icon",

        @Column
        @Lob
        var description: String,

        @ManyToMany()
        @JoinTable(
            name                = "shopping_list_category__product_category",
            joinColumns         = [JoinColumn(name = "shopping_list_category_id")],
            inverseJoinColumns  = [JoinColumn(name = "product_category_id")]
        )
        val productCategories: MutableList<ProductCategory> = mutableListOf(),


        @ElementCollection
        @CollectionTable(
            name = "shopping_list_category__foursquare_id",
            joinColumns = [JoinColumn(name = "shopping_list_category_id")]
        )
        @Column
        val foursquareCategoryIds: MutableList<String> = mutableListOf()
) {
    companion object {
        const val SHOPPING_LIST_CATEGORY_UNIQUE_NAME_CONSTRAINT = "shopping_list_category_unique_name_constraint"
    }

    override fun toString() = "ShoppingListCategory(name='$name')"


}
