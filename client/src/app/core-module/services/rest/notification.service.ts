import { Injectable } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { Notification } from "../../models/notification";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class NotificationService extends MyRestService<Notification> {

    constructor(
        httpClient: HttpClient
    ) {
        super('users/me/notifications', httpClient);
    }


    markNotificationsSentUntilDateRead(date: Date): Observable<any> {
        return this.httpClient.post(`${this.resourcePath}/lastSeen`, date);
    }
}
