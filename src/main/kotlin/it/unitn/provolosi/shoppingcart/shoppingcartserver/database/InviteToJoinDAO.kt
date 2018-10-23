package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User

interface InviteToJoinDAO {
    fun save(inviteToJoin: InviteToJoin): InviteToJoin

    fun findByEmail(email: String): List<InviteToJoin>
}