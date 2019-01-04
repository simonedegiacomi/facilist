import { Component, OnInit } from '@angular/core';
import { PagedResult } from "../../core-module/services/rest/MyRestService";
import { Notification } from "../../core-module/models/notification";
import { NotificationSyncService } from "../../core-module/services/sync/notification-sync.service";
import { NotificationService } from "../../core-module/services/rest/notification.service";
import { PushSubscriptionService } from "../../core-module/services/push-subscription.service";

const $ = window['jQuery'];

/**
 * Component that shows the notifications. Whent he notificartion box is closed only a button (with a notification counter)
 * is visible. Clicking the button open the box and mark the notifications as read.
 */
@Component({
    selector: 'app-notifications',
    templateUrl: './notifications.component.html',
    styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

    /**
     * Status of the box
     */
    open = false;

    /***
     * Flag that indicates if the user has enabled the Push API. This is needed to show a box that invites the user
     * to enable notifications.
     */
    hasSubscribed: boolean;

    /**
     * List of notifications in the box
     */
    notifications: Notification[];

    /**
     * Current page
     */
    private currentPage: PagedResult<Notification>;

    private lastOpenTime: Date;

    constructor(
        private notificationService: NotificationService,
        private notificationSyncService: NotificationSyncService,
        private pushNotificationService: PushSubscriptionService
    ) { }

    /**
     * List of unread notifications
     */
    get unreadNotifications(): Notification[] {
        if (this.notifications == null) {
            return [];
        }

        return this.notifications.filter(n => n.seenAt == null);
    }

    /**
     * Number of unread notifications
     */
    get unreadNotificationsCount(): number {
        return this.unreadNotifications.length;
    }

    ngOnInit() {
        this.fetchNotifications();
        this.listenForNewNotifications();

        this.hasSubscribed = this.pushNotificationService.hasActivatedPushNotification();
    }

    private fetchNotifications() {
        this.notificationService.getAllPaged().subscribe(page => {
            this.currentPage   = page;
            this.notifications = page.content;
        });
    }

    private listenForNewNotifications() {
        this.notificationSyncService.newNotification()
            .subscribe(n => {
                this.notifications.splice(0, 0, n)

                if (this.shouldShowNativeNotification()) {
                    this.showNativeNotification(n);
                }
            });
    }

    /**
     * Checks if the app should generate a native notifications.
     */
    private shouldShowNativeNotification() {
        // Show the native notification if the browser tab is not visible and the user granted the notification permission
        return document.hidden && this.pushNotificationService.hasActivatedPushNotification();
    }

    /**
     * Shows a native notification using the browser API
     * @param notification
     */
    private showNativeNotification(notification: Notification) {
        // @ts-ignore
        new window.Notification(notification.message);
    }

    onSubscribeToPushNotifications() {
        this.pushNotificationService.enableOrUpdateSubsciprion().subscribe(
            () => this.hasSubscribed = true,
            (err) => {
                console.error(err)
                alert("Si è verificato un errore durante l'attivazione delle notifiche")
            });
    }

    onToggleNotificationBox() {
        if (this.open) {
            this.open = false;
            this.onNotificationBoxClosed();
        } else {
            this.open = true;
            this.closeNavbar();
            this.onNotificationBoxOpened();
        }
    }

    private closeNavbar () {
        const box = $('.notifications-box-content-container');
        box.addClass('visible');
        $('.navbar-collapse').collapse('hide');
        setTimeout(() => box.removeClass('visible'),350);
    }

    private onNotificationBoxOpened () {
        if (this.unreadNotificationsCount > 0) {
            this.markUnreadNotificationsAsRead();
        }
    }

    private onNotificationBoxClosed () {
        this.lastOpenTime = null;
    }

    private markUnreadNotificationsAsRead() {
        this.lastOpenTime = new Date();
        this.notificationService.markNotificationsSentUntilDateRead(this.lastOpenTime).subscribe(() => {
            this.unreadNotifications.forEach(notification => notification.seenAt = this.lastOpenTime);
        });
    }

    /**
     * Checks if the specified notification has not been read yet or if it has just been read (the seenAt is equal to the
     * time when the box was opened)
     * @param notification
     */
    isUnreadOrJustRead(notification: Notification): boolean {
        return notification.seenAt == null || notification.seenAt == this.lastOpenTime;
    }

}
