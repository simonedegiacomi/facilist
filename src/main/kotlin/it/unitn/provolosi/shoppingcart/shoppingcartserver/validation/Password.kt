package it.unitn.provolosi.shoppingcart.shoppingcartserver.validation

import javax.validation.Constraint
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.reflect.KClass

/**
 * Custo annotation to validate the password with custom properties (min length)
 */
@NotNull
@Size(min = 6)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class Password(
        val message: String = "password is invalid",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Any>> = []
)
