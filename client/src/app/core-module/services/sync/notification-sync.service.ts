import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import { MySyncService } from "./MySyncService";
import { Observable } from "rxjs";
import { Notification } from "../../models/notification";
import { map } from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class NotificationSyncService extends MySyncService {

    constructor(
        stompService: StompService
    ) {
        super(stompService);
    }

    newNotification(): Observable<Notification> {
        return this.subscribe<Notification>("/user/queue/notifications")
            .pipe(
                map(event => event.model)
            );
    }

}
