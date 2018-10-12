package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.import

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ProductCategoryDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.UserDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ProductCategory
import org.apache.commons.csv.CSVFormat
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class ImportService(

        private val passwordEncoder: PasswordEncoder,

        private val userDAO: UserDAO,
        private val productCategoryDAO: ProductCategoryDAO

) {

    val userByEmail     = mutableMapOf<String, User>()
    val categoryByName  = mutableMapOf<String, ProductCategory>()

    fun importDataFromResources() {
        println("\nImport started")

        importUsers()
        importProductCategories()

        printStatistics()
    }

    private fun importUsers () = csvFile("/import/users.csv").forEach {
        val user = User(
            firstName       = it["firstName"],
            lastName        = it["lastName"],
            email           = it["email"],
            password        = passwordEncoder.encode(it["password"]),
            role            = if (it["admin"]!!.toBoolean()) User.ADMIN else User.USER,
            emailVerified   = true
        )
        userDAO.save(user)
        userByEmail[user.email] = user
    }

    private fun importProductCategories () = csvFile("/import/categories.csv").forEach {
        val category = ProductCategory(
            name        = it["name"],
            description = it["description"],
            icon        = "default-product-category-icon"
        )
/*
        val iconFileName = it["icon"]
        if (iconFileName.isNotEmpty()) {
            category.icon = importImageIfExists("product-category-icons", iconFileName)
        }*/

        productCategoryDAO.save(category)
        categoryByName[category.name] = category
    }

    private fun printStatistics () {
        println("\nImport completed")
        println("Users:                     ${userByEmail.size}")
        //println("Products:                  ${productByName.size}")
        println("Product categories:        ${categoryByName.size}")
        /*println("Shopping lists:            $shoppingLists")
        println("Shopping list categories:  ${shoppingListCategoriesByName.size}\n")*/
        println("\n\n")
    }

    private fun csvFile (name: String) = CSVFormat.EXCEL
            .withDelimiter(';')
            .withFirstRecordAsHeader()
            .parse(InputStreamReader(ImportService::class.java.getResourceAsStream(name)))

}
