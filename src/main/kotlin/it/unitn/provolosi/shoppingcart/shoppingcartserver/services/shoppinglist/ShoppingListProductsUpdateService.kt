package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist

import it.unitn.provolosi.shoppingcart.shoppingcartserver.database.ShoppingListProductsUpdateDAO
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProductUpdatesGroup
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.ShoppingListProduct
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.User
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification.NotificationService
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ShoppingListProductsUpdateTask.Companion.DEFAULT_SCHEDULE_INTERVAL
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
        val list            = product.shoppingList
        val previousUpdate  = shoppingListProductsUpdateDAO.findByShoppingListOrNull(list)
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
    @Transactional fun checkRecentUpdates () = shoppingListProductsUpdateTask.onTick()
}

@Component
class ShoppingListProductsUpdateTask (
        private val shoppingListProductsUpdateDAO: ShoppingListProductsUpdateDAO,
        private val notificationService: NotificationService
) {

    companion object {
        const val DEFAULT_RECENT_THRESHOLD_INTERVAL  = 10 * 1000
        const val DEFAULT_SCHEDULE_INTERVAL          = 1000
    }

    @Transactional
    fun onTick () {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, -DEFAULT_RECENT_THRESHOLD_INTERVAL)

        shoppingListProductsUpdateDAO.findByLastEditAtLessThan(calendar.time).forEach { recentUpdate ->

            buildAndSendNotification(recentUpdate)

            deleteGroup(recentUpdate)
        }
    }

    private fun deleteGroup (group: ShoppingListProductUpdatesGroup) {
        shoppingListProductsUpdateDAO.delete(group)
    }

    private fun buildAndSendNotification (group: ShoppingListProductUpdatesGroup) {
        val users = group.users
                .asSequence()
                .map { user -> user.firstName }
                .joinToString(separator = ",")

        val products = group.updatedProducts
                .asSequence()
                .take(3) // TODO: Declare constant
                .map { product -> product.product.name }
                .joinToString(separator = ",")
        // TODO: Improve message
        val message =  "$users modificato $products"

        notificationService.saveAndSend(
            group.shoppingList.ownerAndCollaborators().map { user -> Notification(
                target  = user,
                message = message,
                icon    = group.shoppingList.icon
            ) }
        )
    }

}