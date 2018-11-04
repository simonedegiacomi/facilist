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

    constructor(
        private notificationService: NotificationService,
        private notificationSyncService: NotificationSyncService,
        private pushNotificationService: PushSubscriptionService
    ) {
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
            .subscribe(n => this.notifications.splice(0, 0, n));
    }


    onSubscribeToPushNotifications() {
        this.pushNotificationService.enableOrUpdateSubsciprion().subscribe(
            () => this.hasSubscribed = true,
            () => alert("Si è verificato un errore durante l'attivazione delle notifiche"));
    }

}
