package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.*
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.i18n.TranslationUtils
import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.shoppinglist.ShoppingListProductsUpdateTask
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Utility class to generate localized notifications
 */
@Component
class ShoppingListNotifications(
        /**
         * Service to persist and send the notifications
         */
        private val notificationService: NotificationService,

        /**
         * Website url (used to generate links in notifications)
         */
        @Value("\${websiteUrl}")
        private val websiteUrl: String
) {

    /**
     * Given the string key, this function returns the localized string in the specified locale
     */
    private fun translate(user: User, messageKey: String) = TranslationUtils
            .getNotificationsTranslationMap(user.locale)[messageKey]

    /**
     * Sends the notification to send to collaborators when a list is deleted
     * @param user: User that deleted the list (the owner)
     */
    fun notifyCollaboratorsListDeleted(user: User, list: ShoppingList) = notificationService.saveAndSend(
        list.ownerAndCollaboratorsExcept(user)
                .map { u ->
                    Notification(
                        message = "${user.firstName} ${translate(u, "hasDeletedTheList")} \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = websiteUrl
                    )
                }
    )

    /**
     * Sends the notification to send when the list info are updated (list name, icon or description)
     * @param user The user who modified the information
     */
    fun notifyCollaboratorsListUpdated(user: User, list: ShoppingList) = notificationService.saveAndSend(
        list.ownerAndCollaboratorsExcept(user)
                .map { u ->
                    Notification(
                        message = "${user.firstName} ${translate(u, "hasUpdatedTheList")} \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = "$websiteUrl/shoppingLists/${list.id}"
                    )
                }
    )

    /**
     * Send the notification to send to the collaborators of the relative list except the user who sent the message
     */
    fun notifyCollaboratorsNewMessage(message: ChatMessage) = notificationService.saveAndSend(
        message.shoppingList.ownerAndCollaboratorsExcept(message.user)
                .map { u ->
                    Notification(
                        target  = u,
                        message = "${message.user.firstName} ${translate(u, "hasSentANewMessage")} \"${message.shoppingList.name}\"",
                        icon    = message.shoppingList.icon,
                        url     = "/user/shoppingLists/${message.shoppingList.id}"
                    )
                }
    )

    /**
     * Send the notification that notifies the user of the specified collaborator that he is been added to the list
     * @param inviter User who invited the new collaborator to the list
     * @param collaboration New collaborations
     */
    fun notifyNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ${translate(collaboration.user, "hasInvitedYouToCollaborate")} \"${list.name}\"",
            icon    = inviter.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }

    /**
     * Sends a notification to each collaborator of the list about the new collaborator.
     * The notification will not be sent to the user who invited the new collaborator and to the new collaborator.
     */
    fun notifyCollaboratorsNewCollaborator(inviter: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val invited = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != inviter && u != invited }
                .map { u ->
                    Notification(
                        message = "${inviter.firstName} ${translate(u, "hasInvited")} ${invited.firstName} ${translate(u, "toCollaborateToTheList")} \"${list.name}\"",
                        target  = u,
                        icon    = inviter.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }


    /**
     * Sends the notification to a collaborator that has been removed from a list
     */
    fun notifyCollaboratorCollaborationDeleted(user: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${user.firstName} ${translate(collaboration.user, "hasRemovedYouFromTheCollaborators")} ${list.name}",
            icon    = user.photo,
            target  = collaboration.user,
            url     = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }

    /**
     * Notifies collaborators of the specified list that a collaborator has been removed
     */
    fun notifyCollaboratorsCollaborationDeleted(user: User, collaboration: ShoppingListCollaboration) {
        val list    = collaboration.shoppingList
        val removed = collaboration.user

        val notifications = list
                .ownerAndCollaborators()
                .filter { u -> u != user && u != removed }
                .map { u ->
                    Notification(
                        message = "${user.firstName} ${translate(u, "hasRemoved")} ${removed.firstName} dai collaboratoridella lista \"${list.name}\"",
                        target  = u,
                        icon    = user.photo,
                        url     = "$websiteUrl/shoppingLists/{$list.id}"
                    )
                }

        notificationService.saveAndSend(notifications)
    }

    /**
     * Sends the notification to the user who roles has been changed in the specified list
     */
    fun notifyCollaboratorRoleChanged(inviter: User, collaboration: ShoppingListCollaboration) {
        val list = collaboration.shoppingList

        notificationService.saveAndSend(Notification(
            message = "${inviter.firstName} ${translate(collaboration.user, "hasChangedYourRole")} \"${list.name}\"",
            icon = inviter.photo,
            target = collaboration.user,
            url = "$websiteUrl/shoppingLists/{$list.id}"
        ))
    }

    /**
     * Send the notification about a group of products in the list that have been edited.
     */
    fun notifyCollaboratorsListProductsUpdated (group: ShoppingListProductUpdatesGroup) = notificationService.saveAndSend(
        getDestinationUsersOfListProductsUpdatedNotification(group)
                .map { user ->
                    Notification(
                        target = user,
                        message = buildMessageOfListProductsUpdatedNotification(user, group),
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

    private fun buildMessageOfListProductsUpdatedNotification(target: User, group: ShoppingListProductUpdatesGroup): String {
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
        message += " " + (if (group.users.size == 1) translate(target, "has") else translate(target, "theyHave"))
        message += " ${translate(target, "edited")} $products"
        if (products.length < group.updatedProducts.size) {
            message += " ..."
        }
        return message
    }
}

/**
 * Return a list of users which are the owner and the collaborator of the list, except a speicifed user
 * @param user: USer to esclude from the list
 */
private fun ShoppingList.ownerAndCollaboratorsExcept(user: User) = ownerAndCollaborators().filter { u -> u != user }
