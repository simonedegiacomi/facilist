package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.EmailAlreadyInUseException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User.Companion.USER_EMAIL_UNIQUE_NAME_CONSTRAINT
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

interface InternalSpringJPAUserDAO : JpaRepository<User, Long> {
    fun getUserByEmail(email: String): User

    @Query("SELECT u FROM User u where u.email LIKE %:email% AND u.role = 'ROLE_USER' ORDER BY u.firstName ASC")
    fun findUserByEmailContainingIgnoreCase(email: String): List<User>
}

@Component
class SpringJPAUserDAO(
        val springRepository: InternalSpringJPAUserDAO
) : UserDAO {


    override fun save(user: User) = runAndMapConstraintFailureTo(
        USER_EMAIL_UNIQUE_NAME_CONSTRAINT,
        { EmailAlreadyInUseException() },
        { springRepository.save(user) }
    )

    override fun getUserByEmail(email: String) = try {
        springRepository.getUserByEmail(email)
    } catch (ex: EmptyResultDataAccessException){
        throw UserNotFoundException()
    }

    override fun findUserByEmailContainingIgnoreCaseLimit10(email: String) =
            springRepository.findUserByEmailContainingIgnoreCase(email).take(10)
}