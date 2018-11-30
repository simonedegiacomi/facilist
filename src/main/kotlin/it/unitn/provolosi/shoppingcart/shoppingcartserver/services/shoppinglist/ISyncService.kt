package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*

interface ISyncService {

    fun userNewShoppingList(user: User, list: ShoppingList)

    fun shoppingListInfoEdited(list: ShoppingList)

    fun shoppingListDeleted(list: ShoppingList)


    fun newShoppingListProduct(relation: ShoppingListProduct)

    fun productInShoppingListEdited(relation: ShoppingListProduct)
    
    fun productInShoppingListDeleted(relation: ShoppingListProduct)


    fun newMessageInShoppingList(message: ChatMessage)


    fun newCollaborator(collaboration: ShoppingListCollaboration)

    fun collaborationEdited(collaboration: ShoppingListCollaboration)

    fun collaborationDeleted(collaboration: ShoppingListCollaboration)
}