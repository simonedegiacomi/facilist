package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.htmlemailengine

import org.springframework.stereotype.Component
import java.io.File


@Component
class CachedMJMLHTMLEmailEngine : HTMLEmailEngine {

    companion object {
        const val compiledDir = "/emails/output"
    }

    override fun render(file: String, variables: Map<String, String>) = getResourceFile("$compiledDir/$file.html")
            .readText()
            .replace(Regex("\\{\\{ ([a-zA-Z]*) }}")) { match -> variables.getOrDefault(match.groupValues[1], "?") }

    fun getResourceFile(path: String) = File(CachedMJMLHTMLEmailEngine::class.java.getResource(path).toURI())
}
