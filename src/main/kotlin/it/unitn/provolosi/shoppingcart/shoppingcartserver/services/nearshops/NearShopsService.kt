package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops

import com.fasterxml.jackson.annotation.JsonIgnore

interface NearShopsService {
    fun findShopsOfCategoryNearCoordinates(categories: List<String>, coordinates: Coordinates): List<NearShops>
    fun getCategories (): List<NearShopCategory>
}

data class NearShops (
        val name: String
)

data class Coordinates(
        val lat: Double,
        val lon: Double
)


data class NearShopCategory (

        @JsonIgnore
        val categories: List<NearShopCategory>,

        val id: String,

        val name: String
)