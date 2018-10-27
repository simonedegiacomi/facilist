package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

/*

@RestController
@RequestMapping("/api/shoppingLists")
class UpdateProducts(
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val productDAO: ProductDAO
) {

    @PostMapping("/{id}/products")
    @RolesAllowed(User.USER)
    fun updateProducts(
            @AppUser user: User,
            @PathVariable id: Long,
            @RequestBody @Valid update: List<ProductInListDTO>
    ): ResponseEntity<ShoppingList> = try {
        val list = shoppingListDAO.findById(id)

        if (list.isUserOwnerOrCollaborator(user)) {
            shoppingListProductDAO.deleteAll(list.products)
            list.products.clear()

            update.map { it ->
                shoppingListProductDAO.save(ShoppingListProduct(
                    product         = productDAO.findById(it.productId!!), // TODO: Check if the product is compatible
                    shoppingList    = list,
                    bought           = it.bought!!,
                    quantity        = it.quantity!!,
                    image           = it.image!!,
                    note            = it.note
                ))
            }
            // TODO: Verify if i need to add the ShoppingListProduct to the list model or if Hibernate will do it

            shoppingListDAO.save(list)

            ResponseEntity(list, HttpStatus.OK)

        } else {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    } catch (ex: ShoppingListNotFoundException) {

        ResponseEntity.notFound().build()
    } catch (ex: ProductNotFoundException) {

        ResponseEntity.notFound().build()
    }

    data class ProductInListDTO(

            @get:NotNull
            val productId: Long?,

            @get:NotNull
            val quantity: Int?,

            @get:NotNull
            val bought: Boolean?,

            val note: String?,

            @get:NotNull
            val image: String?
    )
}*/
