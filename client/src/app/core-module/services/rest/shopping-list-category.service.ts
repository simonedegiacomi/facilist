import { Injectable } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { ShoppingListCategory } from "../../models/shopping-list-category";
import { ProductCategory } from "../../models/product-category";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { ifResponseCodeThen } from "../../utils/request";
import { CONFLICT } from "http-status-codes";
import { ForesquareCategory } from "../../models/foresquare-category";

export const SHOPPING_LIST_CATEGORY_NAME_CONFLICT = "shoppingListCategoryNameConflict";

@Injectable({
    providedIn: 'root'
})
export class ShoppingListCategoryService extends MyRestService<ShoppingListCategory> {

    constructor(
        httpClient: HttpClient
    ) {
        super('shoppingListCategories', httpClient);
    }

    updateProductCategories(category: ShoppingListCategory, included: ProductCategory[]): Observable<void> {
        const url = `${this.resourcePath}/${category.id}/productCategories`;

        const productCategoryIds = included.map(category => category.id);

        return this.httpClient.put<void>(url, productCategoryIds);
    }

    create(entity: ShoppingListCategory): Observable<ShoppingListCategory> {
        return super.create(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, SHOPPING_LIST_CATEGORY_NAME_CONFLICT))
        );
    }


    update(entity: ShoppingListCategory): Observable<ShoppingListCategory> {
        return super.update(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, SHOPPING_LIST_CATEGORY_NAME_CONFLICT))
        );
    }

    getAllForesquareCategories(): Observable<ForesquareCategory[]> {
        return this.httpClient.get<ForesquareCategory[]>(`${this.resourcePath}/foursquareCategories`);
    }
}
