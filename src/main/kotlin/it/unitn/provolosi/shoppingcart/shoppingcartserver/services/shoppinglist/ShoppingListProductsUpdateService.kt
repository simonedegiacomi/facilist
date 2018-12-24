package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductsUpdateDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProductUpdatesGroup
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ShoppingListProductsUpdateTask.Companion.DEFAULT_SCHEDULE_INTERVAL
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class ShoppingListProductsUpdateService(
        private val shoppingListProductsUpdateDAO: ShoppingListProductsUpdateDAO,
        private val shoppingListProductsUpdateTask: ShoppingListProductsUpdateTask
) {

    fun collectEvent(user: User, product: ShoppingListProduct) {
        val list = product.shoppingList
        val previousUpdate = shoppingListProductsUpdateDAO.findByShoppingListOrNull(list)
                ?: ShoppingListProductUpdatesGroup(shoppingList = list)

        with(previousUpdate) {
            addProduct(product)
            addUser(user)
            lastEditAt = Date()
        }

        shoppingListProductsUpdateDAO.save(previousUpdate)
    }


    /**
     * The task is in another class otherwise it won't work. This is because we need to get a database transaction and
     * to make the @Transactional work we need to call a transactional method from another class.
     */
    @Scheduled(fixedRate = DEFAULT_SCHEDULE_INTERVAL.toLong())
    @Transactional fun checkRecentUpdates() = shoppingListProductsUpdateTask.onTick()
}

@Component
class ShoppingListProductsUpdateTask(
        private val shoppingListProductsUpdateDAO: ShoppingListProductsUpdateDAO,
        private val notificationService: NotificationService,

        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    companion object {
        const val DEFAULT_RECENT_THRESHOLD_INTERVAL = 10 * 1000
        const val DEFAULT_SCHEDULE_INTERVAL = 1000

        const val MAX_PRODUCTS_NAME_IN_NOTIFICATION = 3
    }

    @Transactional
    fun onTick() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, -DEFAULT_RECENT_THRESHOLD_INTERVAL)

        shoppingListProductsUpdateDAO.findByLastEditAtLessThan(calendar.time).forEach { recentUpdate ->
            buildAndSendNotification(recentUpdate)

            deleteGroup(recentUpdate)
        }
    }

    private fun deleteGroup(group: ShoppingListProductUpdatesGroup) {
        shoppingListProductsUpdateDAO.delete(group)
    }

    private fun buildAndSendNotification(group: ShoppingListProductUpdatesGroup) {
        val notifications = buildNotifications(group)

        notificationService.saveAndSend(notifications)
    }

    private fun buildNotifications(group: ShoppingListProductUpdatesGroup) = getDestinationUsersOfNotification(group)
            .map { user ->
                Notification(
                    target = user,
                    message = buildMessage(group),
                    icon = group.shoppingList.icon,
                    url = "/user/shoppingLists/${group.shoppingList.id}"
                )
            }

    /**
     * Return a list of users that collaborate in the list. If only one user have edited products of the list, that user
     * will not be present in the list
     */
    private fun getDestinationUsersOfNotification(group: ShoppingListProductUpdatesGroup): List<User> {
        val collaborators = group.shoppingList
                .ownerAndCollaborators()
                .toMutableList()

        if (group.users.size == 1) {
            collaborators.removeAll(group.users)
        }

        return collaborators
    }

    private fun buildMessage(group: ShoppingListProductUpdatesGroup): String {
        val users = group.users
                .asSequence()
                .map { user -> user.firstName }
                .joinToString(separator = ", ")

        val products = group.updatedProducts
                .asSequence()
                .take(MAX_PRODUCTS_NAME_IN_NOTIFICATION)
                .map { product -> product.product.name }
                .joinToString(separator = ", ")

        var message = users
        message += if (group.users.size == 1) " ha" else " hanno"
        message += " modificato $products"
        if (products.length < group.updatedProducts.size) {
            message += " ..."
        }
        return message
    }
}

