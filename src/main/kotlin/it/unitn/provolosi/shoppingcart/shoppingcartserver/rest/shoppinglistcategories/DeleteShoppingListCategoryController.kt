package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shoppingListCategories")
class DeleteShoppingListCategoryController(
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO
) {

    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable id: Long
    ): ResponseEntity<Any> = try {

        shoppingListCategoryDAO.deleteById(id)
        ResponseEntity.ok().build()
    } catch (ex: ShoppingListCategoryNotFoundException) {

        ResponseEntity.notFound().build()
    }
}