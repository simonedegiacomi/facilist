import { Injectable } from '@angular/core';
import { AuthService } from "../auth.service";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";

@Injectable()
export class UserGuard implements CanActivate {

    constructor(
        private auth: AuthService,
        private router: Router
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.auth.getUser().pipe(

            map(user => user != null && !user.admin),

            // If there's an error the user is not logged in
            catchError(_ => of(false)),

            // Redirect the user to the login page if he can't access the route
            tap(isLoggedInAndIsUser => {
                if (!isLoggedInAndIsUser) {
                    this.auth.unauthorizedRoute = state.url;
                    this.router.navigateByUrl('/home?showLoginModal=true');
                }
            })
        );
    }


}
