package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration

class ShoppingListCollaborationNotFoundException : Exception()
class UserAlreadyCollaboratesWithShoppingListException : Exception()

interface ShoppingListCollaborationDAO {

    fun save(collaboration: ShoppingListCollaboration): ShoppingListCollaboration

    fun findById(id: Long): ShoppingListCollaboration

    fun deleteById(id: Long)
}