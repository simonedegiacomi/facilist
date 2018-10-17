import { Injectable } from '@angular/core';
import { ProductCategory } from "../models/product-category";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { CONFLICT } from "http-status-codes";
import { ifResponseCodeThen } from "../utils";
import { MyRestService } from "./MyRestService";
import { HttpClient } from "@angular/common/http";

export const CATEGORY_NAME_CONFLICT = "categoryNameConflict";


@Injectable({
    providedIn: 'root'
})
export class ProductCategoryService extends MyRestService<ProductCategory> {


    constructor(
        httpClient: HttpClient
    ) {
        super('productCategories', httpClient);
    }


    update(entity: ProductCategory): Observable<ProductCategory> {
        return super.update(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, CATEGORY_NAME_CONFLICT))
        );
    }

    create(entity: ProductCategory): Observable<ProductCategory> {
        return super.create(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, CATEGORY_NAME_CONFLICT))
        );
    }


}
