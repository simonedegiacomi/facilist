package it.unitn.provolosi.shoppingcart.shoppingcartserver

import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.UserArgumentResolver
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingListResolver
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.import.ImportService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest


fun main(args: Array<String>) {
    runApplication<ShoppingcartServerApplication>(*args)
}

@SpringBootApplication
@EnableScheduling
class ShoppingcartServerApplication(
        private val import: ImportService
) {


    @PostConstruct
    fun onInitialized() {
        import.importDataFromResources()
    }

}

@Configuration
class Config(
        private val userResolver: UserArgumentResolver,
        private val shoppingListResolver: PathVariableBelongingShoppingListResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userResolver)
        resolvers.add(shoppingListResolver)
    }

}

/**
 * The ExceptionController below is used to serve the SPA Angular application:
 * - If the requested file is index.html or a resource file (js, css, etc...) Spring will take care of that (Spring
 *  will serve the static files from the /resources/public folder);
 * - If the request path is matched by one of the restController, the right controller will handle the request;
 * - If no handler is found there will be an error. Spring will call getErrorPath method of this class and then
 * the handleError404 will send the index.html file, without sending a redirect.
 */
@RestController
class ExceptionController:ErrorController {

    companion object {
        const val ERROR_PATH = "/error"
    }

    @RequestMapping(ERROR_PATH)
    fun handleError404(request: HttpServletRequest, e: Exception) = ResponseEntity.ok(InputStreamResource(
        ShoppingcartServerApplication::class.java.getResourceAsStream("/public/index.html")
    ))

    override fun getErrorPath(): String {
        return ERROR_PATH
    }
}
