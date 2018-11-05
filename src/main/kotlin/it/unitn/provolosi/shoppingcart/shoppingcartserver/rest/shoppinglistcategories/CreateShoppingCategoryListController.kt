package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglistcategories

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListCategoryWithSameNameAlreadyExistsException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCategory
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/shoppingListCategories")
class CreateShoppingCategoryListController(
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO
) {


    @PostMapping
    @RolesAllowed(User.ADMIN)
    fun create(
            @RequestBody @Valid dto: ShoppingListCategoryDTO
    ): ResponseEntity<ShoppingListCategory> = try {
        ResponseEntity(shoppingListCategoryDAO.save(ShoppingListCategory(
            name                    = dto.name!!,
            description             = dto.description!!,
            icon                    = dto.icon!!,
            foursquareCategoryIds   = dto.foursquareCategoryIds!!.toMutableList()
        )), HttpStatus.CREATED)

    } catch (ex: ShoppingListCategoryWithSameNameAlreadyExistsException) {

        ResponseEntity.status(HttpStatus.CONFLICT).build()
    }


    data class ShoppingListCategoryDTO(
            @get:NotNull
            @get:NotEmpty
            val name: String?,

            @get:NotNull
            @get:NotEmpty
            val description: String?,

            @get:NotNull
            @get:NotEmpty
            val icon: String?,

            @get:NotNull
            val foursquareCategoryIds: List<String>?
    )
}
