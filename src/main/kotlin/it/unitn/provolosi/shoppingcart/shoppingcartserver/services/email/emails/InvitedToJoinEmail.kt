package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.InviteToJoin
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail

class InvitedToJoinEmail(
        invite: InviteToJoin
) : ResourceEmail() {
    override val to = invite.email

    override val subject = "Iscriviti a $applicationName, i tuoi amici ti stanno aspettando!"

    override val emailTemplateName = "invited-to-list/invited-to-list"

    override val data = mapOf(
        "applicationName"   to applicationName,
        "inviterName"       to invite.inviter.firstName,
        "listName"          to invite.shoppingList.name,
        "link"              to websiteUrl
    )
}