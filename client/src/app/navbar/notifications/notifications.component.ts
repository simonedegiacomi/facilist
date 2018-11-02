import { Component, OnInit } from '@angular/core';
import { NotificationService } from "../../core-module/services/notification.service";
import { PagedResult } from "../../core-module/services/MyRestService";
import { Notification } from "../../core-module/models/notification";

@Component({
    selector: 'app-notifications',
    templateUrl: './notifications.component.html',
    styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

    notifications: Notification[];

    private currentPage: PagedResult<Notification>;

    constructor(
        private notificationService: NotificationService
    ) {
    }

    ngOnInit() {
        this.fetchNotifications();
    }

    private fetchNotifications() {
        this.notificationService.getAllPaged().subscribe(page => {
            this.currentPage   = page;
            this.notifications = page.content;
        });
    }

}
