package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.VerificationTokenDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface InternalSpringJPAVerificationTokenDAO: JpaRepository<VerificationToken, Long> {
    fun findByToken(tokenString: String): VerificationToken
}

@Component
class SpringJPAVerificationTokenDAO (
        private val springRepository: InternalSpringJPAVerificationTokenDAO
) : VerificationTokenDAO {
    override fun save(token: VerificationToken) = springRepository.save(token)

    override fun findByToken(tokenString: String) = springRepository.findByToken(tokenString)

    override fun delete(token: VerificationToken) = springRepository.delete(token)
}