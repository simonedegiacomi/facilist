package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail
import org.springframework.web.util.UriComponentsBuilder

class ConfirmAccountEmail (
        private val token: VerificationToken
): ResourceEmail() {

    override val emailTemplateName = "confirm-account/confirm-account"

    override val data = mapOf(
            "applicationName"   to applicationName,
            "firstName"         to token.user.firstName,
            "link"              to generateConfirmationLink()
        )

    override val to = token.user.email

    override val subject = "$applicationName - Conferma il tuo account"

    private fun generateConfirmationLink() = UriComponentsBuilder
            .fromHttpUrl("$websiteUrl/verifyEmail/{email}")
            .queryParam("token", token.token)
            .buildAndExpand(mapOf("email" to token.user.email))
            .toUriString()

}