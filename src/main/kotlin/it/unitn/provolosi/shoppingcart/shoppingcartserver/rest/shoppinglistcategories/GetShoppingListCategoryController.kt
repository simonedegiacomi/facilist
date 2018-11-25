package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingListCategories")
class GetShoppingListCategoryController (
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO
) {

    @GetMapping()
    fun getAll () = ResponseEntity.ok(shoppingListCategoryDAO.findAllByOrderByNameAsc())

}

