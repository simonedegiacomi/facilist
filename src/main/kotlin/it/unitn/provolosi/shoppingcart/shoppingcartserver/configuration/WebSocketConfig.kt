package it.unitn.provolosi.shoppingcart.shoppingcartserver.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
class WebSocketConfig:WebSocketMessageBrokerConfigurer {
}