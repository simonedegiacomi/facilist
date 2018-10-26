package it.unitn.provolosi.shoppingcart.shoppingcartserver.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class AppWebSocketSecurityConfig: AbstractSecurityWebSocketMessageBrokerConfigurer() {

    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry?) {
        messages!!
                .anyMessage().authenticated()
                .simpSubscribeDestMatchers("/*").authenticated()
    }



    override fun sameOriginDisabled(): Boolean {
        return true
    }

}