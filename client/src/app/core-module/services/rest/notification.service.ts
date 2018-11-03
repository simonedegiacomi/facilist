import { Injectable } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { Notification } from "../../models/notification";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NotificationService extends MyRestService<Notification> {

    constructor(
        httpClient: HttpClient
    ) {
        super('notifications', httpClient);
    }


}
