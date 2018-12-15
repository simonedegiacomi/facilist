package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface InternalSpringJPAInviteToJoinDAO : JpaRepository<InviteToJoin, Long> {
    fun findByEmail(email: String): List<InviteToJoin>
}

@Component
class SpringJPAInviteToJoinDAO(
        private val springRepository: InternalSpringJPAInviteToJoinDAO
) : InviteToJoinDAO {

    override fun save(inviteToJoin: InviteToJoin) = springRepository.save(inviteToJoin)


    override fun findByEmail(email: String) = springRepository.findByEmail(email)

    override fun deleteById(id: Long) = try {
        springRepository.deleteById(id)
    } catch (ex: EmptyResultDataAccessException){
        throw InviteToJoinNotFoundException()
    }
}