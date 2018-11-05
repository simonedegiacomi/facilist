package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories.foresquarecategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.foursquare.FoursquareService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingListCategories/foresquareCategories")
class GetForesquareCategoriesController (
        private val foresquareService: FoursquareService
) {

    @GetMapping
    @RolesAllowed(User.ADMIN)
    fun getCategories () = ResponseEntity.ok(getShopsForesquareCategories())

    fun getShopsForesquareCategories () = foresquareService.getCategories()
            .find { category -> category.name == "Shop & Service" }!!.categories
}