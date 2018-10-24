package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ChangePasswordRequestNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
interface InternalSpringJPAChangePasswordRequestDAO : JpaRepository<ChangePasswordRequest, Long> {

    fun findByNewEmailAndTokenToken(email: String, tokenString: String): Optional<ChangePasswordRequest>

}

@Component
class SpringJPAChangePasswordRequestDAO(
        private val springRepository: InternalSpringJPAChangePasswordRequestDAO
) : ChangePasswordRequestDAO {

    override fun save(request: ChangePasswordRequest) = springRepository.save(request)


    override fun findByNewEmailAndTokenByToken(
            email: String,
            tokenString: String
    ): ChangePasswordRequest = springRepository.findByNewEmailAndTokenToken(
        email,
        tokenString
    ).orElseThrow { ChangePasswordRequestNotFoundException() }


    override fun delete(req: ChangePasswordRequest) = springRepository.delete(req)
}