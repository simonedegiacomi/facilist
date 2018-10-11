import { Injectable, Injector } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { ShoppingListCategory } from "../models/shopping-list-category";
import { ProductCategory } from "../models/product-category";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { ifResponseCodeThen } from "../utils";
import { CONFLICT } from "http-status-codes";

export const SHOPPING_LIST_CATEGORY_NAME_CONFLICT = "shoppingListCategoryNameConflict";

@Injectable({
    providedIn: 'root'
})
export class ShoppingListCategoryService extends MyRestService<ShoppingListCategory> {

    constructor(
        private httpClient: HttpClient,
        injector: Injector
    ) {
        super(ShoppingListCategory, 'shoppingListCategories', injector);
    }

    updateProductCategories(category: ShoppingListCategory, included: ProductCategory[]): Observable<void> {
        const links = included
            .map(included => included._links.self.href)
            .join("\n");
        const options = {
            headers: new HttpHeaders().append('Content-Type', 'text/uri-list')
        };

        return this.httpClient.put<void>(category._links.productCategories.href, links, options)
    }

    getProductCategoriesOfShoppingListCategory(category: ShoppingListCategory): Observable<ProductCategory[]> {
        return category.getRelationArray(ProductCategory, 'productCategories');
    }

    create(entity: ShoppingListCategory): Observable<Observable<never> | ShoppingListCategory> {
        return super.create(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, SHOPPING_LIST_CATEGORY_NAME_CONFLICT))
        );
    }


    update(entity: ShoppingListCategory): Observable<Observable<never> | ShoppingListCategory> {
        return super.update(entity).pipe(
            catchError(ifResponseCodeThen(CONFLICT, SHOPPING_LIST_CATEGORY_NAME_CONFLICT))
        );
    }
}
