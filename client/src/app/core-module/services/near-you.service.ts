import { Injectable } from '@angular/core';
import { from, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { UserService } from "./rest/user.service";

import geodist from 'geodist';

/**
 * Distance to user to consider that the user moved enough to notify the server
 */
const DISTANCE_THRESHOLD = 500; // meters

/**
 * Service that handle the location permission and sends the location to the server when the user starts the app
 * or moves more than a specified threshold
 */
@Injectable({
    providedIn: 'root'
})
export class NearYouService {

    /**
     * Last position sent to the server
     */
    private lastPosition: Position;

    constructor(
        private userService: UserService
    ) { }

    hasGivenPermissionToUseGeolocation(): Observable<boolean> {
        // @ts-ignore
        return from(navigator.permissions.query({name: 'geolocation'})).pipe(
            map(permission => {
                // @ts-ignore
                return permission.state === 'granted'
            })
        );
    }

    /**
     * Use the browser API to ask the user the permission for the geolocation
     */
    askPermission(): Observable<boolean> {
        return Observable.create((observer) => {
            navigator.geolocation.getCurrentPosition(() => {
                observer.next(true);
            })
        });
    }

    /**
     * Start the service listening to position events. When the first positions found or the user moves more than a
     * threshold, the user position is sent to the server.
     */
    start() {
        navigator.geolocation.watchPosition((position: Position) => {
            if (this.shouldSendNewPosition(position)) {
                console.log("[NEAR-YOU] updating user position");
                this.userService.updatePosition(position)
                    .subscribe(() => this.lastPosition = position);
            } else {
                console.log("[NEAR-YOU] ignoring position event");
            }
        });
    }

    /**
     * Checks if the specified position is far from the last position sent to the server more than a threshold
     * @param position
     */
    shouldSendNewPosition(position: Position): boolean {
        if (this.lastPosition == null) {
            return true;
        }

        const distance = geodist(
            this.getGeodistObject(position),
            this.getGeodistObject(this.lastPosition),
            { unit: 'meters'}
        );

        return distance > DISTANCE_THRESHOLD;
    }

    getGeodistObject (position: Position) {
        return {
            lat: position.coords.latitude,
            lon: position.coords.longitude
        };
    }
}
