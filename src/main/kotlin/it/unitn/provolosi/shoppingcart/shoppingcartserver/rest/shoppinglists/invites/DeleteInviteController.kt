package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.invites

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.InviteToJoinNotFoundException
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.AppUser
import it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.shoppinglists.PathVariableBelongingShoppingList
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shoppingLists/{shoppingListId}/invites")
class DeleteInviteController(
        private val inviteToJoinDAO: InviteToJoinDAO,
        private val syncShoppingListService: SyncService
        ) {


    @DeleteMapping("/{inviteId}")
    fun deleteInvite(
            @PathVariableBelongingShoppingList list: ShoppingList,
            @AppUser user: User,
            @PathVariable inviteId: Long
    ): ResponseEntity<Any> = try {

        inviteToJoinDAO.deleteById(inviteId)

        syncShoppingListService.inviteDeleted(list, inviteId)

        ResponseEntity.ok().build()
    } catch (ex: InviteToJoinNotFoundException) {
        ResponseEntity.notFound().build()
    }

}