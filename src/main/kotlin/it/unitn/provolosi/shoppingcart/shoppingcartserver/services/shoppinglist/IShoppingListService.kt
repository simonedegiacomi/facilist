package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import javax.servlet.http.HttpServletRequest

class InviterCantEditCollaboratorsException : Exception()

interface IShoppingListService {

    fun addUserToShoppingListByEmail(list: ShoppingList, inviter: User, email: String, req: HttpServletRequest)

    fun acceptInvitesForUser(user: User)

}