import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from "@angular/router";
import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";
import { AuthService } from "../auth.service";

@Injectable()
export class NotLoggedInGuard implements CanActivate {

    constructor(
        private auth: AuthService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.auth.getUser().pipe(

            // If the user is logged in, he can't access the route
            map(_ => false),

            // If the user can't access the route, redirect him to his homepage
            tap (canAccess => {
                if (!canAccess) {
                    this.auth.redirectAfterLogin();
                }
            }),

            // If there's an error the user is not logged in, so he can access the route
            catchError(_ =>  of(true))
        );
    }
}
