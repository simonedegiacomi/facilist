package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email

import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n.TranslationUtils
import java.io.File

interface EmailService {
    fun sendEmail(email: Email)
}

/**
 * Class that models an email that can be sent using a EmailService
 */
abstract class Email {

    val applicationName: String = "Facilist"

    /**
     * NOTE: @Variable or @Autowired of Spring would not work here, because the Email class is not managed by Spring
     */
    val websiteUrl: String = System.getProperty("WEBSITE_URL", "http://localhost:4200")

    abstract val to: String

    abstract fun subject(): String

    abstract fun text(): String

    open fun html() = text()

    override fun toString() = "${subject()}: ${text()}"
}

/**
 * Class that models an email based on resources files. That is, in the res folder there are the localized files with
 * the email as plain text and html.
 * Fields of this email will be compiles: They will be translated and filled with the specified data.
 */
abstract class ResourceEmail : Email() {

    /**
     * Subject tha may contains string to translate
     */
    abstract val subjectToCompile: String

    /**
     * Name of the email, used to choose the file name of the resources to load
     */
    abstract val emailName: String

    /**
     * Data to insert in the email resources files (ex: links, names, etc...)
     */
    abstract val data: Map<String, String>

    /**
     * Locale to use to localize strings
     */
    abstract val locale: String

    override fun text() = loadAndCompile("txt")

    override fun html() = loadAndCompile("html")

    override fun subject () = compileText(subjectToCompile)

    /**
     * Return the map that contains the localized strings and the data values.
     */
    private val dataWithTranslations: Map<String, String>
        get () {
            val map = data.toMutableMap()
            map.putAll(TranslationUtils.getEmailTranslationMap(locale, emailName))
            return map
        }

    /**
     * Load a file and replace placeholders with translated strings and data
     */
    private fun loadAndCompile(extension: String) = compileText(
        loadFile("$emailName/$emailName.$extension").readText()
    )

    /**
     * Compile a text: find placeholders and replace them with the translated string and the data
     */
    private fun compileText(text: String) = text
            .replace(Regex("\\{\\{ ([a-zA-Z]*) }}")) { dataWithTranslations.getOrDefault(it.groupValues[1], "?") }

    /**
     * Given the file name returns the content of it as a string
     */
    private fun loadFile(relativeFileName: String) = File(
        EmailService::class.java.getResource("/emails/$relativeFileName").toURI())

}