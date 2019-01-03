package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import conflict
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid

@RestController
@RequestMapping("/api/shoppingListCategories")
class EditShoppingListCategoryController (
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO,
        private val productCategoryDAO: ProductCategoryDAO
) {

    @PutMapping("/{id}")
    @RolesAllowed(User.ADMIN)
    fun create(
            @PathVariable id: Long,
            @RequestBody @Valid dto: CreateShoppingCategoryListController.ShoppingListCategoryDTO
    ): ResponseEntity<ShoppingListCategory> = try {

        val category = shoppingListCategoryDAO.findById(id)

        with(category) {
            name           = dto.name!!
            description    = dto.description!!
            icon           = dto.icon!!

            foursquareCategoryIds.clear()
            foursquareCategoryIds.addAll(dto.foursquareCategoryIds!!)
        }

        ok(shoppingListCategoryDAO.save(category))

    } catch (ex: ShoppingListCategoryNotFoundException) {

        notFound()
    } catch (ex: ShoppingListCategoryWithSameNameAlreadyExistsException) {

        conflict()
    }

    @PutMapping("/{id}/productCategories")
    @RolesAllowed(User.ADMIN)
    fun updateProductCategoriesInShoppingListCategory(
        @PathVariable id: Long,
        @RequestBody productCategoryIds: List<Long>
    ): ResponseEntity<Any> = try {

        val listCategory = shoppingListCategoryDAO.findById(id)

        listCategory.productCategories.clear()
        listCategory.productCategories.addAll(
            productCategoryIds.map { productCategoryId -> productCategoryDAO.findById(productCategoryId) }
        )

        ok(shoppingListCategoryDAO.save(listCategory))
    } catch (ex: ShoppingListCategoryNotFoundException) {

        notFound()
    } catch (ex: ProductCategoryNotFoundException) {

        notFound()
    }

}
