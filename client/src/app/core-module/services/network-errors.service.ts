import { Injectable } from '@angular/core';
import { Observable, throwError } from "rxjs";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { catchError } from "rxjs/operators";

export const NETWORK_ERROR = 'newtorkError';

@Injectable()
export class NetworkErrorsService {

    error: Observable<any>;

    private observer;

    constructor () {
        this.error = new Observable<any>(observer => {
            this.observer = observer;
        });
    }

    onNetworkError (error: any) {
        console.error('Network error', error);

        if (this.observer != null) {
            this.observer.next(error);
        }
    }

    errorHandler<T> (): ((error: any) => Observable<T | any>) {
        return (error: any) => {
            if (NetworkErrorsService.isNetworkError(error)) {
                this.onNetworkError(error);
            }

            return throwError(error);
        }
    }

    static isNetworkError (error: any): boolean {
        if (error === NETWORK_ERROR) {
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
