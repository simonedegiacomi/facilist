import { Component, OnInit } from '@angular/core';
import { PagedResult } from "../../core-module/services/rest/MyRestService";
import { Notification } from "../../core-module/models/notification";
import { NotificationSyncService } from "../../core-module/services/sync/notification-sync.service";
import { NotificationService } from "../../core-module/services/rest/notification.service";
import { PushSubscriptionService } from "../../core-module/services/push-subscription.service";

@Component({
    selector: 'app-notifications',
    templateUrl: './notifications.component.html',
    styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

    open = false;

    hasSubscribed: boolean;

    notifications: Notification[];

    private currentPage: PagedResult<Notification>;

    private lastOpenTime: Date;

    constructor(
        private notificationService: NotificationService,
        private notificationSyncService: NotificationSyncService,
        private pushNotificationService: PushSubscriptionService
    ) {
    }

    get unreadNotifications(): Notification[] {
        if (this.notifications == null) {
            return [];
        }

        return this.notifications.filter(n => n.seenAt == null)
    }

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

    private shouldShowNativeNotification () {
        return document.hidden && this.pushNotificationService.hasActivatedPushNotification();
    }

    private showNativeNotification (notification: Notification) {
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
        this.open = !this.open;

        if (this.open && this.unreadNotificationsCount > 0) {
            this.lastOpenTime = new Date();
            this.notificationService.markNotificationsSentUntilDateRead(this.lastOpenTime).subscribe(() => {
                this.unreadNotifications.forEach(notification => notification.seenAt = this.lastOpenTime);
            });
        } else {
            this.lastOpenTime = null;
        }
    }

    isUnreadOrJustRead (notification: Notification): boolean {
       return notification.seenAt == null ||  notification.seenAt == this.lastOpenTime;
    }

}
