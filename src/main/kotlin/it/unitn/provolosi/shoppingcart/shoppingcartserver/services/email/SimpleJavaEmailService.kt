package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email

import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.Mailer
import org.simplejavamail.mailer.MailerBuilder
import org.simplejavamail.mailer.config.TransportStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Implementation of the email service based on the SimpleJavaEmail library.
 * This service is a Spring components and receives the authentication credentials through the constructor using
 * the Value Spring annotations. This annotation load the actual data from the environment properties file
 */
@Service
class SimpleJavaEmailService (
        @Value("\${emails.server.host}")
        private val serverHost: String,

        @Value("\${emails.server.port}")
        private val serverPort: Int,

        @Value("\${emails.server.username}")
        private val username: String,

        @Value("\${emails.server.password}")
        private val password: String,

        @Value("\${emails.server.sender}")
        private val sender: String,

        /**
         * Flag that disable the actual sending of the email and just print the email on the console (useful during development)
         */
        @Value("\${emails.sendEmails}")
        private val sendEmails: Boolean
): EmailService {

    val mailer: Mailer = MailerBuilder
            .withSMTPServer(serverHost, serverPort, username, password)
            .withTransportStrategy(TransportStrategy.SMTPS)
            .buildMailer()


    override fun sendEmail(email: Email) {
        val simpleJavaEmail = EmailBuilder
                .startingBlank()
                .from(sender)
                .to(email.to)
                .withSubject(email.subject())
                .withPlainText(email.text())
                .withHTMLText(email.html())
                .buildEmail()

        if (sendEmails) {
            mailer.sendMail(simpleJavaEmail, true)
        }

        println("Sent email: ${email.text()}")
    }
}
