package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid

@RestController
@RequestMapping("/shoppingListCategories")
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

        category.name           = dto.name!!
        category.description    = dto.description!!
        category.icon           = dto.icon!!

        shoppingListCategoryDAO.save(category)

        ResponseEntity(category, HttpStatus.OK)

    } catch (ex: ShoppingListCategoryNotFoundException) {

        ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    } catch (ex: ShoppingListCategoryWithSameNameAlreadyExistsException) {

        ResponseEntity.status(HttpStatus.CONFLICT).build()
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


        shoppingListCategoryDAO.save(listCategory)

        ResponseEntity.status(HttpStatus.OK).build()
    } catch (ex: ShoppingListCategoryNotFoundException) {

        ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    } catch (ex: ProductCategoryNotFoundException) {

        ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

}
