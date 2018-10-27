package it.unitn.provolosi.shoppingcart.shoppingcartserver.database.springjpa

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

interface InternalSpringJPAShoppingListDAO : JpaRepository<ShoppingList, Long> {

    @Query("SELECT sl FROM ShoppingList sl WHERE sl.creator = :user OR EXISTS (SELECT c FROM ShoppingListCollaboration c WHERE c.user = :user AND c.shoppingList = sl)")
    fun getShoppingListsWithUserAsCreatorOrCollaborator(user: User): List<ShoppingList>
}

@Component
class SpringJPAShoppingListDAO(
        private val springRepository: InternalSpringJPAShoppingListDAO
) : ShoppingListDAO {

    override fun getShoppingListPreviewsByUser(user: User) = springRepository
            .getShoppingListsWithUserAsCreatorOrCollaborator(user)
            .map { it -> it.toPreview() }

    override fun save(shoppingList: ShoppingList) = springRepository.save(shoppingList)

    override fun findById(id: Long): ShoppingList = springRepository.findById(id)
            .orElseThrow { ShoppingListNotFoundException() }


    override fun delete(list: ShoppingList) = springRepository.delete(list)
}