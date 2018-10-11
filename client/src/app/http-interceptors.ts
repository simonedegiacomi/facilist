import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NetworkErrorInterceptor } from "./core-module/services/network-errors.service";
import { AuthHttpInterceptor } from "./core-module/services/auth.service";


export const HttpInterceptorsProvider = [
    { provide: HTTP_INTERCEPTORS, useClass: NetworkErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true },
];
