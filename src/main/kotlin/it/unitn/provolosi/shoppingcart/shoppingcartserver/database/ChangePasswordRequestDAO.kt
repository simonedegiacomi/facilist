package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest

class ChangePasswordRequestNotFoundException : Exception()

interface ChangePasswordRequestDAO {

    fun save(request: ChangePasswordRequest): ChangePasswordRequest

    fun findByNewEmailAndTokenByToken(email: String, tokenString: String): ChangePasswordRequest

    fun delete(req: ChangePasswordRequest)

}