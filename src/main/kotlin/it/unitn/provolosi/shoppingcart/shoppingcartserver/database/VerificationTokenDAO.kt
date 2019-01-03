package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken

class VerificationTokenNotFoundException : Exception()

interface VerificationTokenDAO {

    fun save(verificationToken: VerificationToken): VerificationToken

    fun findByToken(tokenString: String): VerificationToken

    fun delete(token: VerificationToken)

}