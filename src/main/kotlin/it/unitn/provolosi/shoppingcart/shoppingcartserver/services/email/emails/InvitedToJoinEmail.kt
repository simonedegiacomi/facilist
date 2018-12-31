package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail

class InvitedToJoinEmail(
        invite: InviteToJoin
) : ResourceEmail() {
    override val to = invite.email

    override val subjectToCompile = "{{ registerTo }} $applicationName, {{ yourFriendsAreWaitingForYou }}"

    override val emailName = "invited-to-list"

    /**
     * We expect that the friend invited speaks the same language as the inviter
     */
    override val locale = invite.inviter.locale

    override val data = mapOf(
        "applicationName"   to applicationName,
        "inviterName"       to invite.inviter.firstName,
        "listName"          to invite.shoppingList.name,
        "link"              to websiteUrl
    )
}