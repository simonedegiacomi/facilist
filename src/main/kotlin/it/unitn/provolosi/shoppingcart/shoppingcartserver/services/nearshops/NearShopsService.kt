package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.nearshops

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Service that find shops near to a user. The shops which will be search are only those in which the user is interested.
 * That is, only shops that sells things in the shopping list categories of lists of the user will be found.
 */
interface NearShopsService {

    fun findShopsOfCategoryNearCoordinates(categories: List<String>, coordinates: Coordinates): List<NearShops>

    /**
     * Return the list of categories of shops that can be found. Useful in the administration interface to assign categories
     * to the shopping lists
     */
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