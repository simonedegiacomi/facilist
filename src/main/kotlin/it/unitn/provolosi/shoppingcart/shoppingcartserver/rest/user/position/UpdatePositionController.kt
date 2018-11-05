package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user.position

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.foursquare.Coordinates
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.foursquare.FoursquareService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.foursquare.NearShops
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.WebSocketDeliveryMethod
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/users/me/position")
class UpdatePositionController (
        private val shoppingListDAO: ShoppingListDAO,
        private val foursquareService: FoursquareService,
        private val notificationSync: WebSocketDeliveryMethod
) {

    @PostMapping
    fun updatePosition (
            @RequestBody @Valid update: UpdatePositionDTO,
            @AppUser user: User
    ) {
        val foursquareCategoryIds = shoppingListDAO.getShoppingListPreviewsByUser(user)
                .filter { list -> list.itemsToBuy > 0 }
                .map { list -> list.shoppingList.category }
                .distinctBy { shoppingListCategory -> shoppingListCategory.id }
                .flatMap { shoppingListCategory -> shoppingListCategory.foursquareCategoryIds }
                .distinct()

        val shops = foursquareService.findShopsOfCategoryNearCoordinates(
            foursquareCategoryIds,
            update.toCoordinates()
        )

        sendNotification(user, shops)

    }

    data class UpdatePositionDTO (
            @get:NotNull
            val lat: Double?,

            @get:NotNull
            val lon: Double?
    ) {
        fun toCoordinates () = Coordinates(lat!!, lon!!)
    }

    private fun sendNotification (user: User, shops: List<NearShops>) {
        val shopNames = shops
                .asSequence()
                .map { near -> near.name }
                .joinToString(separator = ", ")

        notificationSync.deliver(Notification(
            message = "Non farti scappare l'occasione, passa da $shopNames",
            icon    = "",
            target  = user
        ))
    }
}
