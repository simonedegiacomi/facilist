package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangeEmailRequest

class ChangePasswordRequestNotFoundException : Exception()

interface ChangePasswordRequestDAO {

    fun save(request: ChangeEmailRequest): ChangeEmailRequest

    fun findByNewEmailAndTokenByToken(email: String, tokenString: String): ChangeEmailRequest

    fun delete(req: ChangeEmailRequest)

}