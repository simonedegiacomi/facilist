import { Injectable } from '@angular/core';
import { Observable, throwError } from "rxjs";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { catchError } from "rxjs/operators";

export const NETWORK_ERROR = 'networkError';

/**
 * Service used to collect all the network and server errors, so they can be handled and showed to the user through the
 * NetworkErrorDisplay component.
 */
@Injectable()
export class NetworkErrorsService {

    /**
     * Observer used by the NetworkErrorDusplay component
     */
    error: Observable<any>;

    /**
     * Observer which events are emitted by the public error Observer
     */
    private observer;

    constructor () {
        this.error = new Observable<any>(observer => {
            this.observer = observer;
        });
    }

    /**
     * Method called by the NetworkErrorInterceptor when a network error happens
     * @param error
     */
    onNetworkError (error: any) {
        console.error('Network error', error);

        if (this.observer != null) {
            this.observer.next(error);
        }
    }

    /**
     * Returns a new error handler that can be passed as argument to the catchError function of RXJS
     */
    errorHandler<T> (): ((error: any) => Observable<T | any>) {
        return (error: any) => {
            if (NetworkErrorsService.isNetworkError(error)) {
                this.onNetworkError(error);
            }

            return throwError(error);
        }
    }

    /**
     * Utility function that checks if an error is a network or a server error
     * @param error
     */
    static isNetworkError (error: any): boolean {

        if (error === NETWORK_ERROR) { // The error was handled by our logic in some service
            return true;
        }

        if (error instanceof HttpErrorResponse ) {
            if (Math.floor(error.status / 100) === 5) { // Consider server error as network error
                return true;
            }

            if (error.status === 0) { // Network error
                return true;
            }
        }

        if (error instanceof ErrorEvent) {
            return true;
        }

        return false;
    }
}

/**
 * HTTP request filter that looks for 500 response code
 */
@Injectable()
export class NetworkErrorInterceptor implements HttpInterceptor {

    constructor (
        private networkErrorService: NetworkErrorsService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError(this.networkErrorService.errorHandler())
        )
    }

}
