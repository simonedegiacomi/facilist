package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.import

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.security.ADMIN
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class ImportService(

        private val passwordEncoder: PasswordEncoder,
        private val userDAO: UserDAO

) {

    fun importDataFromResources() {
        userDAO.save(User(
            firstName       = "Simone",
            lastName        = "Degiacomi",
            email           = "simonedegiacomi97@gmail.com",
            role            = ADMIN,
            password        = passwordEncoder.encode("prova"),
            emailVerified   = true
        ))
    }
}
