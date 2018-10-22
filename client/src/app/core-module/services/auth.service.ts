import { Injectable } from '@angular/core';
import {
    HttpClient,
    HttpEvent,
    HttpHandler,
    HttpHeaders,
    HttpInterceptor,
    HttpParams,
    HttpRequest
} from "@angular/common/http";
import { BehaviorSubject, Observable, of, throwError } from "rxjs";
import { Roles, User } from "../models/user";
import { catchError, map, tap } from "rxjs/operators";
import { NETWORK_ERROR } from "./network-errors.service";
import { BAD_REQUEST, FORBIDDEN } from "http-status-codes";
import { Router } from "@angular/router";

export const USER_NOT_LOGGED_IN   = "userNotLoggedIn";
export const WRONG_CREDENTIALS    = "wrongCredentials";
export const EMAIL_NOT_REGISTERED = "emailNotRegistered";
export const INVALID_CODE         = "invalidCode";

@Injectable()
export class AuthService {

    /**
     * Cached user value
     */
    public user: User = null;

    /**
     * Observable logged in user
     */
    public readonly user$ = new BehaviorSubject<User>(null);


    // Last route  which access was unauthorized (used for redirect after login)
    public unauthorizedRoute: string;

    constructor(
        private httpClient: HttpClient,
        private router: Router
    ) { }


    getUser(redirectAfter: boolean = false): Observable<User> {
        if (this.user != null) {
            return of(this.user);
        }

        return this.httpClient.get<User>("/api/users/me").pipe(
            map(user => new User(user)),
            tap(user => this.notifyUserObservers(user)),
            tap(user => console.log("Current user:", user)),
            tap(_ => {
                if (redirectAfter) {
                    this.redirectAfterLogin();
                }
            }),

            catchError(res => {
                if (res.status == FORBIDDEN) {
                    this.notifyUserObservers(null);
                    return throwError(USER_NOT_LOGGED_IN);
                }
                return throwError(res);
            })
        );
    }

    private notifyUserObservers (user: User) {
        this.user = user;
        this.user$.next(user);
    }


    login(email: string, password: string, rememberMe: boolean): Observable<User> {
        this.notifyUserObservers(null);

        const body = new URLSearchParams();
        body.set('email', email);
        body.set('password', password);
        if (rememberMe) {
            body.set('rememberMe', 'true');
        }

        const headers = {
            headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
        };

        return this.httpClient.post<any>("/api/auth/login", body.toString(), headers).pipe(
            tap(() => console.info('Logged in')),
            tap(() => this.getUser(true).subscribe()),

            catchError(res => throwError(res.status == FORBIDDEN ? WRONG_CREDENTIALS : NETWORK_ERROR))
        );
    }


    verifyEmail(email: string, token: string): Observable<any> {
        const options = {
            params: new HttpParams().set('token', token)
        };

        return this.httpClient.post(`/api/auth/verifyEmail/${email}`, {}, options);
    }

    recoverPassword(email: string): Observable<any> {
        return this.httpClient.post('/api/auth/recoverPassword', {email}).pipe(
            catchError(res => throwError(res.status == BAD_REQUEST ? EMAIL_NOT_REGISTERED : NETWORK_ERROR)),
        );
    }


    logout(): Observable<void> {
        if (this.user == null) {
            return;
        }

        return this.httpClient.post<void>('/api/auth/logout', null).pipe(
            tap(() => this.notifyUserObservers(null))
        );
    }

    completeRecoverPassword(data: { email: string; newPassword: string; token: string }): Observable<any> {
        return this.httpClient.post('/api/auth/completeRecoverPassword', data).pipe(
            catchError(res => throwError(res.status == BAD_REQUEST ? INVALID_CODE : NETWORK_ERROR))
        );
    }


    redirectAfterLogin () {
        if (this.user === null) {
            console.warn('Accessed AuthService.redirectAfterLogin when no user is logged in');
            this.router.navigate(['/home']);
            return;
        }

        let redirectTo = Roles.isAdmin(this.user) ? '/admin/dashboard' : '/user';
        if (this.unauthorizedRoute != null) {
            redirectTo = this.unauthorizedRoute;
        }

        this.router.navigateByUrl(redirectTo)
            .then(() => this.unauthorizedRoute = null);
    }
}

@Injectable()
export class AuthHttpInterceptor implements HttpInterceptor {

    constructor (
        private auth: AuthService,
        private router: Router
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError(err => {

                if (err.status === FORBIDDEN && AuthHttpInterceptor.isNotALoginRequest(req)) {
                    const currentRoute          = this.router.routerState.snapshot.url;
                    this.auth.unauthorizedRoute = currentRoute;
                    this.auth.user              = null;

                    this.router.navigateByUrl('/home?openLoginModal=true')
                }

                return throwError(err);
            })
        )
    }

    static isNotALoginRequest (req: HttpRequest<any>) {
        return !(req.url.startsWith('/api/users/me') || req.url.startsWith('/api/auth/login'));
    }

}
