package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.emails

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.VerificationToken
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email.ResourceEmail
import org.springframework.web.util.UriComponentsBuilder

class RecoverPasswordEmail(
        private val token: VerificationToken
) : ResourceEmail() {

    override val emailTemplateName = "recover-password/recover-password"

    override val to = token.user.email

    override val subject = "$applicationName - Password dimenticata"

    override val data: Map<String, String> = mapOf(
        "applicationName" to applicationName,
        "link" to generateConfirmationLink()
    )

    private fun generateConfirmationLink() = UriComponentsBuilder
            .fromHttpUrl("$websiteUrl/recoverPassword/{email}")
            .queryParam("token", token.token)
            .buildAndExpand(mapOf("email" to token.user.email))
            .toUriString()
}