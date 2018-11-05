package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.foursquare

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL

@Component
class FoursquareService(
        @Value("\${foursquareAPI.clientId}")
        private val clientId: String,

        @Value("\${foursquareAPI.clientSecret}")
        private val clientSecret: String
) {

    fun findShopsOfCategoryNearCoordinates(categories: List<String>, coordinates: Coordinates): List<NearShops> {
        val latLon = coordinates.toFoursquareLatLon()
        val url = URL(
            "https://api.foursquare.com/v2/venues/search?" +
                    "client_id=$clientId&" +
                    "client_secret=$clientSecret&" +
                    "v=20181103&" +
                    "ll=$latLon&" +
                    "radious=250&" +
                    "categoryId=${categories.joinToString(separator = ",")}&" +
                    "limit=3"
        )

        // TODO: Handle errors
        return ObjectMapper().readTree(url)["response"]["venues"]
                .map { venue -> NearShops(venue["name"].asText()) }
    }

    fun getCategories ():List<FoursquareCategory> {
        val url = URL(
            "https://api.foursquare.com/v2/venues/categories?" +
                    "client_id=$clientId&" +
                    "client_secret=$clientSecret&" +
                    "v=20181103"
        )

        val rootCategories = ObjectMapper().readTree(url)["response"]["categories"]
        return readCategories(rootCategories)
    }


    private fun readCategories(categories: JsonNode):List<FoursquareCategory> = categories.map { category ->
        FoursquareCategory(
            name        = category["name"].asText(),
            id          = category["id"].asText(),
            categories  = if (category["categories"] == null) emptyList() else readCategories(category["categories"])
        )
    }


}

data class NearShops (
        val name: String
)

data class FoursquareCategory (

        val categories: List<FoursquareCategory> = emptyList(),

        val id: String,

        val name: String
)

data class Coordinates(
        val lat: Double,
        val lon: Double
) {
    fun toFoursquareLatLon() = "$lat,$lon"
}