package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email

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

    abstract val subject: String

    abstract fun text(): String

    open fun html() = text()

    override fun toString() = "$subject: ${text()}"
}

abstract class ResourceEmail : Email() {

    abstract val emailTemplateName: String

    abstract val data: Map<String, String>

    override fun text() = loadAndRender("txt")

    override fun html() = loadAndRender("html")

    private fun loadAndRender(extension: String) = loadFile("$emailTemplateName.$extension")
            .readText()
            .replace(Regex("\\{\\{ ([a-zA-Z]*) }}")) { match -> data.getOrDefault(match.groupValues[1], "?") }

    private fun loadFile(relativeFileName: String) = File(
        EmailService::class.java.getResource("/emails/$relativeFileName").toURI())

}