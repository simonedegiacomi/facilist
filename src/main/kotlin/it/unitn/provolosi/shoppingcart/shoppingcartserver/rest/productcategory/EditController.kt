package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.productcategory

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryWithSameNameAlreadyExistsException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.validation.Valid


@RestController
@RequestMapping("/productCategories")
class EditController (
        private val productCategoryDAO: ProductCategoryDAO
) {

    @PutMapping("/{id}")
    @RolesAllowed(User.ADMIN)
    fun edit(
            @PathVariable id: Long,
            @RequestBody @Valid category: ProductCategory
    ): ResponseEntity<ProductCategory> = try {
        val updated = updateCategory(id, category)
        ResponseEntity(updated, HttpStatus.OK)

    } catch (e: ProductCategoryNotFoundException) {
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    } catch (e: ProductCategoryWithSameNameAlreadyExistsException) {
        ResponseEntity.status(HttpStatus.CONFLICT).build()

    }


    private fun updateCategory (id: Long, update: ProductCategory): ProductCategory {
        val category = productCategoryDAO.findById(id)

        category.name           = update.name
        category.description    = update.description
        category.icon           = update.icon

        productCategoryDAO.save(category)
        return category
    }
}