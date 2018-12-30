package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.import

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images.ImagesService
import org.apache.commons.csv.CSVFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

@Component
class ImportService(

        private val passwordEncoder: PasswordEncoder,

        private val imagesService: ImagesService,

        @Value("\${uploads.folder}")
        private val uploadsFolderPath: String,

        private val userDAO: UserDAO,
        private val productDAO: ProductDAO,
        private val productCategoryDAO: ProductCategoryDAO,
        private val shoppingListCategoryDAO: ShoppingListCategoryDAO,
        private val shoppingListDAO: ShoppingListDAO,
        private val shoppingListCollaborationDAO: ShoppingListCollaborationDAO,
        private val shoppingListProductDAO: ShoppingListProductDAO,
        private val chatMessageDAO: ChatMessageDAO

) {

    val userByEmail                     = mutableMapOf<String, User>()
    val categoryByName                  = mutableMapOf<String, ProductCategory>()
    val productByName                   = mutableMapOf<String, Product>()
    val shoppingListCategoriesByName    = mutableMapOf<String, ShoppingListCategory>()
    var shoppingListsByName             = mutableMapOf<String, ShoppingList>()
    var chatMessages                        = 0

    fun importDataFromResources() {
        println("\nImport started")

        importUsers()
        importProductCategories()
        importProducts()
        importShoppingListCategories()
        importShoppingLists()
        importChatMessages()

        importDefaultImages()
        importNotificationImages()

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

        val photoFileName = it["photo"]
        if (photoFileName.isNotEmpty()) {
            user.photo = importImage("user-photos", photoFileName)
        }

        userDAO.save(user)
        userByEmail[user.email] = user
    }

    private fun importProductCategories () = csvFile("/import/categories.csv").forEach {
        val category = ProductCategory(
            name        = it["name"],
            description = it["description"],
            icon        = "default-product-category-icon"
        )

        val iconFileName = it["icon"]
        if (iconFileName.isNotEmpty()) {
            category.icon = importImage("product-category-icons", iconFileName)
        }

        productCategoryDAO.save(category)
        categoryByName[category.name] = category
    }

    private fun importProducts () = csvFile("/import/products.csv").forEach {
        val category = categoryByName[it["category"]]!!
        val product = Product(
            name        = it["name"],
            icon        = category.icon,
            category    = category
        )

        val iconFileName = it["icon"]
        if (iconFileName.isNotEmpty()) {
            product.icon = importImage("product-icons", iconFileName)
        }

        productDAO.save(product)
        productByName[product.name] = product
    }

    private fun importShoppingListCategories () = csvFile("/import/shoppingListCategories.csv").forEach {
        val category = ShoppingListCategory(
            name                    = it["name"],
            description             = it["description"],
            icon                    = "default-shopping-list-category-icon",
            productCategories       = it["productCategories"]
                    .split(",")
                    .map { categoryByName[it]!! }
                    .toMutableList(),
            foursquareCategoryIds   = it["foursquareCategoryIds"].split(",").toMutableList()
        )

        val iconFileName = it["icon"]
        println(iconFileName)
        if (iconFileName.isNotEmpty()) {
            category.icon = importImage("shopping-list-category-icons", iconFileName)
        }

        shoppingListCategoryDAO.save(category)
        shoppingListCategoriesByName[category.name] = category
    }

    private fun importShoppingLists () = csvFile("/import/shoppingLists.csv").forEach {
        val category = shoppingListCategoriesByName[it["category"]]!!
        val shoppingList = ShoppingList(
            name        = it["name"],
            description = it["description"],
            icon        = category.icon,
            creator     = userByEmail[it["owner"]!!]!!,
            category    = shoppingListCategoriesByName[it["category"]]!!
        )
        shoppingListDAO.save(shoppingList)

        it["users"]
                .split(',')
                .forEach {
                    val pieces  = it.split(':')
                    val email   = pieces[0]
                    val role    = pieces[1]

                    shoppingListCollaborationDAO.save(ShoppingListCollaboration(
                        user            = userByEmail[email]!!,
                        shoppingList    = shoppingList,
                        role            = role
                    ))
                }

        it["products"]
                .split(',')
                .forEach {
                    val pieces      = it.split(':')
                    val productName = pieces[0]
                    val toBuy       = pieces[1].toBoolean()
                    val product     = productByName[productName]!!

                    shoppingListProductDAO.save(ShoppingListProduct(
                        product         = product,
                        shoppingList    = shoppingList,
                        bought           = toBuy,
                        image           = product.icon
                    ))
                }

        val iconFileName = it["icon"]
        if (iconFileName.isNotEmpty()) {
            shoppingList.icon = importImage("shopping-list-icons", iconFileName)
        }

        shoppingListsByName[shoppingList.name] = shoppingList
    }

    private fun importChatMessages () = csvFile("/import/chatMessages.csv").forEach {
        chatMessageDAO.save(ChatMessage(
            user = userByEmail[it["user"]]!!,
            shoppingList = shoppingListsByName[it["shoppingList"]]!!,
            message = it["message"]
        ))
        chatMessages++
    }

    private fun importDefaultImages() =
            mapOf(
                "product-category-icons"        to "default-product-category-icon",
                "product-photos"                to "default-product-photo",
                "shopping-list-category-icons"  to "default-shopping-list-category-icon",
                "shopping-list-icons"           to "default-shopping-list-icon",
                "user-photos"                   to "default-user-photo"
            ).forEach { it ->
                val folder = it.key
                val file = it.value
                ImportService::class.java.getResourceAsStream("/import/images/$folder/$file.png")
                        .toFile(File("$uploadsFolderPath/$file.png"))
            }


    private fun  importNotificationImages () = listOf(
        "near-you-notification-icon"
    ).forEach { ImportService::class.java.getResourceAsStream("/import/images/notifications/$it.png")
                .toFile(File("$uploadsFolderPath/$it.png"))
    }

    private fun printStatistics () {
        println("\nImport completed")
        println("Users:                     ${userByEmail.size}")
        println("Products:                  ${productByName.size}")
        println("Product categories:        ${categoryByName.size}")
        println("Shopping lists:            ${shoppingListsByName.size}")
        println("Shopping list categories:  ${shoppingListCategoriesByName.size}")
        println("Chat messages:             $chatMessages")
        println("\n\n")
    }

    private fun csvFile (name: String) = CSVFormat.EXCEL
            .withDelimiter(';')
            .withFirstRecordAsHeader()
            .parse(InputStreamReader(ImportService::class.java.getResourceAsStream(name)))


    private fun importImage(folder: String, name: String) = imagesService.storeImage(
        ImportService::class.java.getResourceAsStream("/import/images/$folder/$name")
    )

    fun InputStream.toFile(file: File) {
        file.outputStream().use { this.copyTo(it) }
    }
}
