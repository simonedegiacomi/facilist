package it.unitn.provolosi.shoppingcart.shoppingcartserver.models

import javax.persistence.*

@Entity()
@Table(name = "shopping_list_category")
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
        val productCategories: List<ProductCategory> = mutableListOf()
)
