package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import notFound
import ok
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingListCategories")
class DeleteShoppingListCategoryController(
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO,
        private val shoppingListDAO: ShoppingListDAO
) {

    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable id: Long
    ): ResponseEntity<Any> = try {

        this.deleteShoppingListsAndCategory(id)
        ok()
    } catch (ex: ShoppingListCategoryNotFoundException) {

        notFound()
    }

    /**
     * Deletes the shopping lists from the database and the shopping list cateogry.
     * TODO: Move into a service
     */
    private fun deleteShoppingListsAndCategory (id: Long) {
        shoppingListDAO.findByCategoryId(id).forEach { shoppingListDAO.delete(it) }
        shoppingListCategoryDAO.deleteById(id)
    }
}