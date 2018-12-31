package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email

import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n.TranslationUtils
import java.io.File

interface EmailService {
    fun sendEmail(email: Email)
}

abstract class Email {

    val applicationName: String = "Facilist"

    /**
     * NOTE: @Variable or @Autowired of Spring would not wrok here, because the Email class is not managed by Spring
     */
    val websiteUrl: String = System.getProperty("WEBSITE_URL", "http://localhost:4200")

    abstract val to: String

    abstract fun subject(): String

    abstract fun text(): String

    open fun html() = text()

    override fun toString() = "${subject()}: ${text()}"
}

abstract class ResourceEmail : Email() {

    abstract val subjectToCompile: String

    abstract val emailName: String

    abstract val data: Map<String, String>

    abstract val locale: String

    override fun text() = loadAndRender("txt")

    override fun html() = loadAndRender("html")

    override fun subject () = compileText(subjectToCompile)

    private val dataWithTranslations: Map<String, String>
        get () {
            val map = data.toMutableMap()
            map.putAll(TranslationUtils.getEmailTranslationMap(locale, emailName))
            return map
        }


    private fun loadAndRender(extension: String) = compileText(
        loadFile("$emailName/$emailName.$extension").readText()
    )


    private fun compileText(text: String) = text
            .replace(Regex("\\{\\{ ([a-zA-Z]*) }}")) { dataWithTranslations.getOrDefault(it.groupValues[1], "?") }

    private fun loadFile(relativeFileName: String) = File(
        EmailService::class.java.getResource("/emails/$relativeFileName").toURI())

}