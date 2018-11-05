import { Injectable } from '@angular/core';
import { from, Observable } from "rxjs";
import { map, tap } from "rxjs/operators";
import { UserService } from "./rest/user.service";

import geodist from 'geodist';

const DISTANCE_THRESHOLD = 500;

@Injectable({
    providedIn: 'root'
})
export class NearYouService {

    private lastPosition: Position;

    constructor(
        private userService: UserService
    ) {
    }

    hasGivenPermissionToUseGeolocation(): Observable<boolean> {
        // @ts-ignore
        return from(navigator.permissions.query({name: 'geolocation'})).pipe(
            map(permission => {
                // @ts-ignore
                return permission.state === 'granted'
            })
        );
    }

    askPermission(): Observable<boolean> {
        return Observable.create((observer) => {
            navigator.geolocation.getCurrentPosition(() => {
                observer.next(true);
            })
        });
    }

    start() {
        navigator.geolocation.watchPosition((position: Position) => {
            console.log('position')
            if (this.shouldSendNewPosition(position)) {
                console.log("[NEAR-YOU] updating user position");
                this.userService.updatePosition(position)
                    .subscribe(() => this.lastPosition = position);
            } else {
                console.log("[NEAR-YOU] ignoring position event");
            }
        });
    }

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
