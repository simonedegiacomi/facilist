package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa


import org.springframework.dao.DataIntegrityViolationException

const val UNKNOWN_DATABASE_ERROR_EXCEPTION_MESSAGE = "unknown database error"

/**
 * Utility function that executes the runnable passed as third parameter.
 * If the runnable doesn't throw any Exception nothing more is done.
 * However, if an exception is throw and if the type of exception is DataIntegrityViolationException,
 * if the exception is about a specified constraint (specified by its name) the handlerConstraintFailure
 * will be executed and the resulting exception will be thrown
 */
fun <T> runAndMapConstraintFailureTo (
        constraintName: String,
        handlerConstraintFailure: () -> Exception,
        runnable: () -> T
): T = try {
    runnable()
} catch (ex: DataIntegrityViolationException) {
    if (ex.toString().contains(constraintName, true)) {
        throw handlerConstraintFailure()
    } else {
        throw RuntimeException(UNKNOWN_DATABASE_ERROR_EXCEPTION_MESSAGE)
    }
}