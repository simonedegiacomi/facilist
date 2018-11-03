import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class UploadService {

    constructor(
        private httpClient: HttpClient
    ) {
    }


    uploadImage(file: File): Observable<string> {
        return this.httpClient.post('/api/images', file, {responseType: 'text'});
    }

}
