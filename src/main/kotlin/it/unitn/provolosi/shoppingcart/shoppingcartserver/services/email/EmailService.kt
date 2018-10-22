package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.email

interface EmailService {
    fun sendEmail(email: Email)
}


abstract class Email {
    abstract fun to(): String

    abstract fun subject(): String

    abstract fun text(): String

    open fun html() = text()

    override fun toString() = "${subject()}: ${text()}"
}

