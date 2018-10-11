package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface UserDAO {
    fun save(user: User)
    fun getUserByEmail(email: String): User
}