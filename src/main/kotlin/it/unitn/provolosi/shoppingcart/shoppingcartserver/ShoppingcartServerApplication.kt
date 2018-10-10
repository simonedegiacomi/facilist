package it.unitn.provolosi.shoppingcart.shoppingcartserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoppingcartServerApplication

fun main(args: Array<String>) {
    runApplication<ShoppingcartServerApplication>(*args)
}
