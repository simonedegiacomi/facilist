package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ShoppingListProductsUpdateTask
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ShoppingListNotifications(
        private val notificationService: NotificationService,

        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {
    // TODO: Move the code to generate the notifications here

    fun notifyCollaboratorsListDeleted(user: User, list: ShoppingList) = notificationService.saveAndSend(
        list.ownerAndCollaboratorsExcept(user)
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha eliminato la lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = websiteUrl
                    )
                }
    )


    fun notifyCollaboratorsListUpdated(user: User, list: ShoppingList) = notificationService.saveAndSend(
        list.ownerAndCollaboratorsExcept(user)
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha modificato la lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = "$websiteUrl/shoppingLists/${list.id}"
                    )
                }
    )

    /**
     * Send a notification to the collaborators of the relative list except the user who sent the message
     */
    fun notifyCollaboratorsNewMessage(message: ChatMessage) = notificationService.saveAndSend(
        message.shoppingList.ownerAndCollaboratorsExcept(message.user)
                .map { user ->
                    Notification(
                        target  = user,
                        message = "${message.user.firstName} ha inviato un nuovo messaggio nella lista \"${message.shoppingList.name}\"",
                        icon    = message.shoppingList.icon,
                        url     = "/user/shoppingLists/${message.shoppingList.id}"
                    )
                }
    )


    fun notifyNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ti ha invitato a collaborare alla lista \"${list.name}\"",
            icon    = inviter.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }


    fun notifyCollaboratorsNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val invited = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != inviter && u != invited }
                .map { u ->
                    Notification(
                        message = "${inviter.firstName} ha invitato ${invited.firstName} a collaborare alla lista \"${list.name}\"",
                        target  = u,
                        icon    = inviter.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }


    fun notifyCollaboratorCollaborationDeleted(user: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${user.firstName} ti ha rimosso dai collaboratori di ${list.name}",
            icon    = user.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }

    fun notifyCollaboratorsCollaborationDeleted(user: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val removed = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != user && u != removed }
                .map { u ->
                    Notification(
                        message = "${user.firstName} ha rimosso ${removed.firstName} dai collaboratoridella lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }

    fun notifyCollaboratorRoleChanged(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ha cambiato i tuoi privilegi nella lista \"${list.name}\"",
            icon = inviter.photo,
            target = collaboration.user,
            url = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }


    fun notifyCollaboratorsListProductsUpdated (group: ShoppingListProductUpdatesGroup) = notificationService.saveAndSend(
        getDestinationUsersOfListProductsUpdatedNotification(group)
                .map { user ->
                    Notification(
                        target = user,
                        message = buildMessageOfListProductsUpdatedNotification(group),
                        icon = group.shoppingList.icon,
                        url = "/user/shoppingLists/${group.shoppingList.id}"
                    )
                }
    )


    /**
     * Return a list of users that collaborate in the list. If only one user have edited products of the list, that user
     * will not be present in the list
     */
    private fun getDestinationUsersOfListProductsUpdatedNotification(group: ShoppingListProductUpdatesGroup): List<User> {
        val collaborators = group.shoppingList
                .ownerAndCollaborators()
                .toMutableList()

        if (group.users.size == 1) {
            collaborators.removeAll(group.users)
        }

        return collaborators
    }

    private fun buildMessageOfListProductsUpdatedNotification(group: ShoppingListProductUpdatesGroup): String {
        val users = group.users
                .asSequence()
                .map { user -> user.firstName }
                .joinToString(separator = ", ")

        val products = group.updatedProducts
                .asSequence()
                .take(ShoppingListProductsUpdateTask.MAX_PRODUCTS_NAME_IN_NOTIFICATION)
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

private fun ShoppingList.ownerAndCollaboratorsExcept(user: User) = ownerAndCollaborators().filter { u -> u != user }