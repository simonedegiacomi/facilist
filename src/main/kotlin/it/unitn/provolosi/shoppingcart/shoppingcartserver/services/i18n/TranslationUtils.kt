package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Utils to load the files that contains the localized strings used on the server side
 */
class TranslationUtils {

    companion object {

        /**
         * Map that maps a locale to all the string localized in that locale
         */
        private val translationMapsByLocale = listOf("it-IT", "en-US")
                .map {
                    it to ObjectMapper().readTree(
                        TranslationUtils::class.java.getResource("/i18n/$it.json"))
                }
                .toMap()

        /**
         * Return the map of localized string of a given locale
         */
        private fun getTranslationMap(locale: String) = translationMapsByLocale[locale]!!

        /**
         * Return the localized strings used in notifications of the specified locale
         */
        fun getNotificationsTranslationMap(locale: String) =
            getTranslationMap(locale)["notifications"]!!.toStringMap()

        /**
         * Return the localized strings used in emails of the specified locale
         */
        fun getEmailTranslationMap(locale: String, emailName: String) =
            getTranslationMap(locale)["emails"]!![emailName]!!.toStringMap()

    }
}

/**
 * Convert a JsonNode (of the Jackson library) to a map of strings
 */
private fun JsonNode.toStringMap(): Map<String, String> = ObjectMapper()
        .convertValue<Map<String, String>>(this, object : TypeReference<Map<String, String>>() {})
