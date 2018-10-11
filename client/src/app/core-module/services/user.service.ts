import { Injectable } from '@angular/core';
import { Observable, throwError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { User } from "../models/user";
import { catchError } from "rxjs/operators";
import { CONFLICT } from 'http-status-codes';
import { NETWORK_ERROR, NetworkErrorsService } from "./network-errors.service";

export const EMAIL_ALREADY_IN_USE = 'emailAlreadyInUse';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(
        private httpClient: HttpClient,
        private networkErrorService: NetworkErrorsService
    ) {
    }


    register(data: any): Observable<User> {
        return this.httpClient
            .post<User>("/api/users/register", data)
            .pipe(
                catchError(res => throwError(res.status == CONFLICT ? EMAIL_ALREADY_IN_USE : NETWORK_ERROR)),
                catchError(this.networkErrorService.errorHandler())
            );
    }
}
