package it.unitn.provolosi.shoppingcart.shoppingcartserver.database

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin

interface InviteToJoinDAO {
    fun save(inviteToJoin: InviteToJoin): InviteToJoin

    fun findByEmail(email: String): List<InviteToJoin>
}