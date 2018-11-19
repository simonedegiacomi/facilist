import { Injectable } from '@angular/core';
import { Observable, throwError } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { User } from "../../models/user";
import { catchError } from "rxjs/operators";
import { CONFLICT, FORBIDDEN } from 'http-status-codes';
import { NETWORK_ERROR, NetworkErrorsService } from "../network-errors.service";
import { MyRestService } from "./MyRestService";
import { WRONG_CREDENTIALS } from "../auth.service";

export const EMAIL_ALREADY_IN_USE = 'emailAlreadyInUse';

@Injectable({
    providedIn: 'root'
})
export class UserService extends MyRestService<User> {

    constructor(
        httpClient: HttpClient,
        private networkErrorService: NetworkErrorsService
    ) {
        super('users', httpClient);
    }


    register(data: any): Observable<User> {
        return this.httpClient
            .post<User>("/api/users/register", data)
            .pipe(
                catchError(res => throwError(res.status == CONFLICT ? EMAIL_ALREADY_IN_USE : NETWORK_ERROR)),
                catchError(this.networkErrorService.errorHandler())
            );
    }

    changeUserPhoto(user: User): Observable<User> {
        return this.httpClient
            .post<User>(`${this.resourcePath}/me/photo`, user.photo);
    }

    changeUserPassword(update: UpdatePassword): Observable<any> {
        return this.httpClient
            .post(`${this.resourcePath}/me/password`, update)
            .pipe(
                catchError(res => throwError(res.status == FORBIDDEN ? WRONG_CREDENTIALS : NETWORK_ERROR))
            );
    }

    changeUserEmail(email: string): Observable<any> {
        return this.httpClient
            .post(`${this.resourcePath}/me/email`, email);
    }

    verifyNewEmail(email: string, token: string): Observable<any> {

        const options = {
            params: new HttpParams().set('token', token)
        };

        return this.httpClient.post(`${this.resourcePath}/confirmEmailChange/${email}`, {}, options);
    }

    updatePosition(position: Position): Observable<any> {
        return this.httpClient
            .post(`${this.resourcePath}/me/position`, {
                lat: position.coords.latitude,
                lon: position.coords.longitude
            });
    }

    findUsersByEmail(email: string): Observable<User[]> {
        return this.httpClient.get<User[]>(`${this.resourcePath}?email=${email}`)
    }
}


export class UpdatePassword {
    currentPassword: string;
    newPassword: string;
}
