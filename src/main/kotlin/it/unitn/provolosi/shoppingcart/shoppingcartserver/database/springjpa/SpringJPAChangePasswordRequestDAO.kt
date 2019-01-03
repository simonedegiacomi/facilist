package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangeEmailRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
interface InternalSpringJPAChangePasswordRequestDAO : JpaRepository<ChangeEmailRequest, Long> {

    fun findByNewEmailAndTokenToken(email: String, tokenString: String): Optional<ChangeEmailRequest>

}

@Component
class SpringJPAChangePasswordRequestDAO(
        private val springRepository: InternalSpringJPAChangePasswordRequestDAO
) : ChangePasswordRequestDAO {

    override fun save(request: ChangeEmailRequest) = springRepository.save(request)


    override fun findByNewEmailAndTokenByToken(
            email: String,
            tokenString: String
    ): ChangeEmailRequest = springRepository.findByNewEmailAndTokenToken(
        email,
        tokenString
    ).orElseThrow { ChangePasswordRequestNotFoundException() }


    override fun delete(req: ChangeEmailRequest) = springRepository.delete(req)
}