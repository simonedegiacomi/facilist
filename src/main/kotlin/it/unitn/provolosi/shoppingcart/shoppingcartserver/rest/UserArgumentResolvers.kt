package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * Annotation to use for method arguments in controller classes to get the currently logged in user.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AppUser

/**
 * Argument resolver that load the currently logged in user from the database, reading the email of the user present in
 * the servlet security context.
 */
@Component()
class UserArgumentResolver(
        private val userDAO: UserDAO
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.hasParameterAnnotation(AppUser::class.java)

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ) = userDAO.getUserByEmail(webRequest.userPrincipal!!.name)

}
