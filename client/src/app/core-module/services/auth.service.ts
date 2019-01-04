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
import { catchError, tap } from "rxjs/operators";
import { NETWORK_ERROR } from "./network-errors.service";
import { BAD_REQUEST, FORBIDDEN, NOT_FOUND } from "http-status-codes";
import { Router } from "@angular/router";

export const USER_NOT_LOGGED_IN   = "userNotLoggedIn";
export const WRONG_CREDENTIALS    = "wrongCredentials";
export const EMAIL_NOT_REGISTERED = "emailNotRegistered";
export const INVALID_CODE         = "invalidCode";

/**
 * Service that handles the authentication of the user
 */
@Injectable()
export class AuthService {

    /**
     * Cached user value
     */
    public user: User = null;

    /**
     * Observable logged in user. Emits a new user (or null) when a user logins or logouts
     */
    public readonly user$ = new BehaviorSubject<User>(null);


    /**
     * Last route which access was unauthorized (used for redirect after login)
     */
    public unauthorizedRoute: string;

    constructor(
        private httpClient: HttpClient,
        private router: Router
    ) { }

    /**
     * Sends a request to get the currently logged in user
     * @param redirectAfter If true and unauthorizedRoute is not null, user will be redirected to unauthorizedRoute if
     *                      authenticated
     */
    getUser(redirectAfter: boolean = false): Observable<User> {
        if (this.user != null) {
            return of(this.user);
        }

        return this.httpClient.get<User>("/api/users/me").pipe(
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

    /**
     * Update thecached value of the user and emits the user event using the BehaviourSubject
     * @param user
     */
    private notifyUserObservers (user: User) {
        this.user = user;
        this.user$.next(user);
    }


    /**
     * Send the POST request to login the user
     * @param email
     * @param password
     * @param rememberMe
     */
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

    /**
     * Completes the user registration verifying the email address
     * @param email
     * @param token
     */
    verifyEmail(email: string, token: string): Observable<any> {
        const options = {
            params: new HttpParams().set('token', token)
        };

        return this.httpClient.post(`/api/users/verifyEmail/${email}`, {}, options);
    }


    logout(): Observable<void> {
        if (this.user == null) {
            return;
        }

        return this.httpClient.post<void>('/api/auth/logout', null).pipe(
            tap(() => this.notifyUserObservers(null))
        );
    }

    /**
     * Send the request to start the recover password procedure
     * @param email
     */
    recoverPassword(email: string): Observable<any> {
        return this.httpClient.post(`/api/users/${email}/recoverPassword`, null).pipe(
            catchError(res => throwError(res.status == NOT_FOUND ? EMAIL_NOT_REGISTERED : NETWORK_ERROR)),
        );
    }

    /**
     * Send the POST to complete the recover password procedure
     * @param data
     */
    completeRecoverPassword(data: { email: string; newPassword: string; token: string }): Observable<any> {
        return this.httpClient.post('/api/users/completeRecoverPassword', data).pipe(
            catchError(res => throwError(res.status == BAD_REQUEST ? INVALID_CODE : NETWORK_ERROR))
        );
    }

    /**
     * Redirect the user to his home (admin home, user home or landing) or to the path in unauthorizedRoute
     */
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

/**
 * Class that intercepts request responses and if a response is a 403 (and it's not a login or status request) redirects
 * the user to the login page
 */
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

    /**
     * Checks if it's a login or a status request
     * @param req
     */
    static isNotALoginRequest (req: HttpRequest<any>) {
        return !(req.url.startsWith('/api/users/me') || req.url.startsWith('/api/auth/login'));
    }

}
