package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.htmlemailengine

interface HTMLEmailEngine {

    fun render(file: String, variables: Map<String, String>): String

}