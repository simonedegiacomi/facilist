package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.products

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingListsProducts/{id}")
class RemoveProductController {

    @DeleteMapping
    fun removeProduct(

    ) {}

}