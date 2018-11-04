package it.unitn.provolosi.shoppingcart.shoppingcartserver

import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.UserArgumentResolver
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingListResolver
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.import.ImportService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct


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
