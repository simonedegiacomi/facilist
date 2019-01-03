package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories.foresquarecategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops.FoursquareNearShopsService
import ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/api/shoppingListCategories/foursquareCategories")
class GetFoursquareCategoriesController (
        private val foresquareService: FoursquareNearShopsService
) {

    @GetMapping
    @RolesAllowed(User.ADMIN)
    fun getCategories () = ok(getShopsFoursquareCategories())

    fun getShopsFoursquareCategories () = foresquareService.getCategories()
            .find { category -> category.name == "Shop & Service" }!!.categories
}