package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

/**
 * Annotation to use to get the shopping list that have the id specified in the path if it belongs to the user
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PathVariableBelongingShoppingList

/**
 * Exception thrown if an id of a shopping list in a url doesn't belong to the user
 */
class UserIsNotACollaboratorException: Exception()

@Component()
class PathVariableBelongingShoppingListResolver(
        private val shoppingListDAO: ShoppingListDAO,
        private val userDAO: UserDAO
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
            parameter.hasParameterAnnotation(PathVariableBelongingShoppingList::class.java)

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {
        // Take the shopping list id from the url path variables
        val request         = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        val pathVariables   = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>
        val shoppingListId  = pathVariables["shoppingListId"]

        if (shoppingListId != null) {
            // Loads the list and the user
            val list = shoppingListDAO.findById(shoppingListId.toLong())
            val user = userDAO.getUserByEmail(webRequest.userPrincipal!!.name)

            // Returns the list if the list exists and the user collaborates in it
            if (list.isUserOwnerOrCollaborator(user)) {
                return list
            } else {
                throw UserIsNotACollaboratorException()
            }
        }

        return null
    }

}

/**
 * Handles errors relative to the argument resolver
 */
@ControllerAdvice
class ShoppingListArgumentResolverExceptionHandler {

    /**
     * Return 404 if ina url it's used the id of a shopping list that doesn't exist
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShoppingListNotFoundException::class)
    fun handleShoppingListNotFoundException() {
    }

    /**
     * Return unauthorized response code if the user tried to access a shopping lists in which he doesn't collaborate
     */
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserIsNotACollaboratorException::class)
    fun handleUserIsNotACollaboratorException() { }

}

