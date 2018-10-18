package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration

interface ShoppingListCollaborationDAO {
    fun save (collaboration: ShoppingListCollaboration): ShoppingListCollaboration
}