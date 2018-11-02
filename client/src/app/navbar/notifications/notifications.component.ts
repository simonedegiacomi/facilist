import { Component, OnInit } from '@angular/core';
import { NotificationService } from "../../core-module/services/notification.service";
import { PagedResult } from "../../core-module/services/MyRestService";
import { Notification } from "../../core-module/models/notification";
import { NotificationSyncService } from "../../core-module/services/notification-sync.service";

@Component({
    selector: 'app-notifications',
    templateUrl: './notifications.component.html',
    styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

    open = false;

    notifications: Notification[];

    private currentPage: PagedResult<Notification>;

    constructor(
        private notificationService: NotificationService,
        private notificationSyncService: NotificationSyncService
    ) {
    }

    ngOnInit() {
        this.fetchNotifications();
        this.listenForNewNotifications();
    }

    private fetchNotifications() {
        this.notificationService.getAllPaged().subscribe(page => {
            this.currentPage   = page;
            this.notifications = page.content;
        });
    }

    private listenForNewNotifications () {
        this.notificationSyncService.newNotification()
            .subscribe(n => this.notifications.splice(0, 0, n));
    }

}
