package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail

class ConfigNewEmailAddressEmail(
        request: ChangePasswordRequest
):ResourceEmail() {

    override val emailTemplateName = "confirm-new-email-address/confirm-new-email-address"

    override val to = request.newEmail

    override val subject = "$applicationName - Conferma nuovo indirizzo"

    override val data = mapOf("" to "")
}