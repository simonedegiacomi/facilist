import { Injectable, Injector } from '@angular/core';
import { ProductCategory } from "../models/product-category";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { CONFLICT } from "http-status-codes";
import { NetworkErrorsService } from "./network-errors.service";
import { ifResponseCodeThen } from "../utils";
import { MyRestService } from "./MyRestService";

export const CATEGORY_NAME_CONFLICT = "categoryNameConflict";


@Injectable({
    providedIn: 'root'
})
export class ProductCategoryService extends MyRestService<ProductCategory> {


    constructor(
        injector: Injector,
        private networkErrorService: NetworkErrorsService,
    ) {
        super(ProductCategory, 'productCategories', injector);
    }

    update(entity: ProductCategory): Observable<ProductCategory> {
        return super.update(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, CATEGORY_NAME_CONFLICT))
        );
    }


    create(entity: ProductCategory): Observable<Observable<never> | ProductCategory> {
        return super.create(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, CATEGORY_NAME_CONFLICT))
        );
    }
}
