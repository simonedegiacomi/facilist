import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";
import { AuthService } from "../auth.service";
import { Roles } from "../../models/user";

/**
 * Guard that make some route accessible only if the logged in user is an admin
 */
@Injectable()
export class AdminGuard implements CanActivate {

    constructor(
        private auth: AuthService,
        private router: Router
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.auth.getUser().pipe(

            map(user => user != null && Roles.isAdmin(user)),

            // If there's an error the user is not logged in
            catchError(_ => of(false)),

            // Redirect the user to the login page if he can't access the route
            tap(isLoggedInAndIsAdmin => {
                if (!isLoggedInAndIsAdmin) {
                    this.auth.unauthorizedRoute = state.url;
                    this.router.navigateByUrl('/home?showLoginModal=true');
                }
            }),
        );
    }
}
