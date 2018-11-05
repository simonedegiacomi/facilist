import { Injectable } from '@angular/core';

export const Permissions = {
    Geolocation: 'geolocation',
    PushAPI: 'pushAPI'
};

@Injectable({
    providedIn: 'root'
})
export class LocalStorageService {

    constructor() {
    }

    public hasPermission(permissionName: string): boolean {
        return window.localStorage.getItem(`permissions.${permissionName}`) === 'true';
    }

    public setPermission(permissionName: string, value: boolean): void {
        window.localStorage.setItem(`permissions.${permissionName}`, JSON.stringify(value));
    }

}
