import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NetworkErrorInterceptor } from "./core-module/services/network-errors.service";
import { AuthHttpInterceptor } from "./core-module/services/auth.service";

/**
 * Here we define the HTTP interceptor that we'll load in the app module.
 * Interceptors are like filters, they get called before or after a HTTP request and can modify it.
 */
export const HttpInterceptorsProvider = [
    { provide: HTTP_INTERCEPTORS, useClass: NetworkErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true },
];
