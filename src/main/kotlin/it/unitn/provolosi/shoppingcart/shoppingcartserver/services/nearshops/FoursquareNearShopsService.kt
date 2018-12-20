package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL

@Component
class FoursquareNearShopsService(
        @Value("\${foursquareAPI.clientId}")
        private val clientId: String,

        @Value("\${foursquareAPI.clientSecret}")
        private val clientSecret: String
) : NearShopsService {

    override fun findShopsOfCategoryNearCoordinates(categories: List<String>, coordinates: Coordinates): List<NearShops> {
        val latLon = coordinates.toFoursquareLatLon()
        val url = URL(
            "https://api.nearshops.com/v2/venues/search?" +
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

    override fun getCategories(): List<NearShopCategory> {
        val url = URL(
            "https://api.nearshops.com/v2/venues/categories?" +
                    "client_id=$clientId&" +
                    "client_secret=$clientSecret&" +
                    "v=20181103"
        )

        val rootCategories = ObjectMapper().readTree(url)["response"]["categories"]
        return readCategories(rootCategories)
    }


    private fun readCategories(categories: JsonNode): List<NearShopCategory> = categories.map { category ->
        NearShopCategory(
            name = category["name"].asText(),
            id = category["id"].asText(),
            categories = readCategories(category["categories"])
        )
    }


}

private fun Coordinates.toFoursquareLatLon() = "$lat,$lon"


