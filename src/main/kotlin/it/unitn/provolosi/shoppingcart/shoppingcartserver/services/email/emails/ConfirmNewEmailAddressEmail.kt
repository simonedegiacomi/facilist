package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ChangeEmailRequest
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail
import org.springframework.web.util.UriComponentsBuilder

class ConfirmNewEmailAddressEmail(
        private val request: ChangeEmailRequest
):ResourceEmail() {

    override val emailName = "confirm-new-email-address"

    override val to = request.newEmail

    override val locale = request.token.user.locale

    override val subjectToCompile = "$applicationName - {{ confirmYourAddress }}"

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