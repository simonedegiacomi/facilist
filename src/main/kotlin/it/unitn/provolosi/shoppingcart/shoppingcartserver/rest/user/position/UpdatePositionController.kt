package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.user.position

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n.TranslationUtils
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops.Coordinates
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops.FoursquareNearShopsService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops.NearShops
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.delivery.WebSocketDeliveryMethod
import org.springframework.beans.factory.annotation.Value
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
        private val foursquareService: FoursquareNearShopsService,
        private val notificationSync: WebSocketDeliveryMethod,

        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    /**
     * Updates the latest position of the user, to notify his about near shops
     */
    @PostMapping
    fun updatePosition (
            @RequestBody @Valid update: UpdatePositionDTO,
            @AppUser user: User
    ) {
        val foursquareCategoryIds = getFoursquareCateogryIdsInWhichUSerIsInterested(user)

        if (foursquareCategoryIds.isNotEmpty()) {
            val shops = foursquareService.findShopsOfCategoryNearCoordinates(
                foursquareCategoryIds,
                update.toCoordinates()
            )

            sendNotification(user, shops)
        }
    }

    data class UpdatePositionDTO (
            @get:NotNull
            val lat: Double?,

            @get:NotNull
            val lon: Double?
    ) {
        fun toCoordinates () = Coordinates(lat!!, lon!!)
    }

    /**
     * Returns a list of ids of Foursquare categories in which the user is interested (category of shops that sell products
     * nedded by the user)
     */
    private fun getFoursquareCateogryIdsInWhichUSerIsInterested (user: User) = shoppingListDAO.getShoppingListPreviewsByUser(user)
            .filter { list -> list.itemsToBuy > 0 }
            .map { list -> list.shoppingList.category }
            .distinctBy { shoppingListCategory -> shoppingListCategory.id }
            .flatMap { shoppingListCategory -> shoppingListCategory.foursquareCategoryIds }
            .distinct()

    private fun sendNotification (user: User, shops: List<NearShops>) {
        val shopNames = shops
                .asSequence()
                .map { near -> near.name }
                .joinToString(separator = ", ")

        val startOfText = TranslationUtils
                .getNotificationsTranslationMap(user.locale)["goNear"]

        notificationSync.deliver(Notification(
            message = "$startOfText $shopNames",
            icon    = "near-you-notification-icon",
            target  = user,
            url     = websiteUrl
        ))
    }
}
