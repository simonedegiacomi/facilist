import { Injectable } from '@angular/core';
import { from, Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { SwPush } from "@angular/service-worker";
import { switchMap, tap } from "rxjs/operators";

// TODO: Ask the public key to the server at runtime
const VAPID_PUBLIC_KEY = "BArkB_1gSLGOPruj4ZC3HeRmq9ncz4vKrDomlNXpOYEeJircD7VPXCY-3Px4MjzhvocOWFmW2c9zy9HHKKbTE4Q";

/**
 * This service manages the Push API subscription asking the user for the permission and sending the subscription info
 * to the server.
 */
@Injectable({
    providedIn: 'root'
})
export class PushSubscriptionService {

    constructor(
        private httpClient: HttpClient,
        private swPush: SwPush
    ) {
    }

    hasActivatedPushNotification (): boolean {
        return window.localStorage.getItem("pushNotificationsActivated") === 'true';
    }

    private setHasActivatedPushNotification (activated: boolean) {
        window.localStorage.setItem("pushNotificationsActivated", activated ? 'true' : 'false');
    }

    enableOrUpdateSubsciprion ():Observable<any> {
        return this.requestPushNotificationPermission().pipe(
            switchMap(subscription => this.sendPushSubscription(subscription)),
            tap(_ => this.setHasActivatedPushNotification(true))
        );
    }

    private requestPushNotificationPermission (): Observable<PushSubscription> {
        return from(this.swPush.requestSubscription({
            serverPublicKey: VAPID_PUBLIC_KEY
        }));
    }

    private sendPushSubscription(subscription: PushSubscription): Observable<any> {
        return this.httpClient.post(`/api/users/me/pushSubscriptions`, {
            endpoint: subscription.endpoint,
            base64PublicKey: arrayBufferToBase64(subscription.getKey('p256dh')),
            base64Auth: arrayBufferToBase64(subscription.getKey('auth'))
        });
    }
}


function arrayBufferToBase64 (data: ArrayBuffer): String {
    return btoa(String.fromCharCode.apply(null, new Uint8Array(data)));
}
