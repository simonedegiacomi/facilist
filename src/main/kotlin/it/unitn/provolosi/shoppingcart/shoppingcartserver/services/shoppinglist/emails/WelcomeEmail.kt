package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.Email

class WelcomeEmail(

        private val collaboration: ShoppingListCollaboration,

        private val inviter: User

): Email() {

    override fun to() = collaboration.user.email

    override fun subject() = "Benvenuto"

    override fun text(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}