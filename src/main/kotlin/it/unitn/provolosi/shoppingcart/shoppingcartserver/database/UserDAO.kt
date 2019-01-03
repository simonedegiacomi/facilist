package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

class EmailAlreadyInUseException : Exception()
class UserNotFoundException : Exception()

interface UserDAO {

    fun save(user: User): User

    fun getUserByEmail(email: String): User

    /**
     * Return a list of at most ten users that satisfy the speicified email filter
     */
    fun findUserByEmailContainingIgnoreCaseLimit10(email: String): List<User>
}