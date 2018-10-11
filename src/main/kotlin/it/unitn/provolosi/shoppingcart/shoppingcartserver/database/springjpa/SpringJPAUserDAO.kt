package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface SpringJPAUserDAO : UserDAO, JpaRepository<User, Long>