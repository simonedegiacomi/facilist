package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangePasswordRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail
import org.springframework.web.util.UriComponentsBuilder

class ConfirmNewEmailAddressEmail(
        private val request: ChangePasswordRequest
):ResourceEmail() {

    override val emailTemplateName = "confirm-new-email-address/confirm-new-email-address"

    override val to = request.newEmail

    override val subject = "$applicationName - Conferma nuovo indirizzo"

    override val data = mapOf(
        "applicationName"   to applicationName,
        "link"              to generateConfirmationLink()
    )

    private fun generateConfirmationLink() = UriComponentsBuilder
            .fromHttpUrl("$websiteUrl/verifyNewEmail/{email}")
            .queryParam("token", request.token.token)
            .buildAndExpand(mapOf("email" to request.newEmail))
            .toUriString()
}