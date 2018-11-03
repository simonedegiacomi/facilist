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


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PathVariableBelongingShoppingList

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

        val request         = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        val pathVariables   = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>
        val shoppingListId  = pathVariables["shoppingListId"]

        if (shoppingListId != null) {
            val list = shoppingListDAO.findById(shoppingListId.toLong())
            val user = userDAO.getUserByEmail(webRequest.userPrincipal!!.name)

            if (list.isUserOwnerOrCollaborator(user)) {
                return list
            } else {
                throw UserIsNotACollaboratorException()
            }
        }

        return null
    }

}

@ControllerAdvice
class ShoppingListArgumentResolverExceptionHandler {


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShoppingListNotFoundException::class)
    fun handleShoppingListNotFoundException() {
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserIsNotACollaboratorException::class)
    fun handleUserIsNotACollaboratorException() {
    }

}

