package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper


class TranslationUtils {

    companion object {
        private val translationMapsByLocale = listOf("it-IT", "en-US")
                .map {
                    it to ObjectMapper().readTree(
                        TranslationUtils::class.java.getResource("/i18n/$it.json"))
                }
                .toMap()

        private fun getTranslationMap(locale: String) = translationMapsByLocale[locale]!!

        private fun jsonNodeToMap(node: JsonNode) =
                ObjectMapper().convertValue<Map<String, String>>(node, object : TypeReference<Map<String, String>>() {})

        fun getNotificationsTranslationMap(locale: String): Map<String, String> = jsonNodeToMap(
            getTranslationMap(locale)["notifications"]!!
        )

        fun getEmailTranslationMap(locale: String, emailName: String): Map<String, String> = jsonNodeToMap(
            getTranslationMap(locale)["emails"]!![emailName]!!
        )
    }
}