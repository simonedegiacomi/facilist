package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListCollaboration
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail

class AddedToListEmail(
        collaboration: ShoppingListCollaboration,
        inviter: User
) : ResourceEmail() {

    override val emailTemplateName = "added-to-list/added-to-list"

    override val to = collaboration.user.email

    override val subject = "$applicationName - Sei stato aggiunto alla lista ${collaboration.shoppingList.name}"

    override val data = mapOf(
        "applicationName"   to applicationName,
        "listName"          to collaboration.shoppingList.name,
        "inviterName"       to inviter.firstName,
        "link"              to "$websiteUrl/user/shoppingLists/${collaboration.shoppingList.id}"
    )
}