import { Product } from "../../models/product";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProductCategory } from "../../models/product-category";
import { MyRestService, PagedResult } from "./MyRestService";
import { HttpClient } from "@angular/common/http";
import { map } from "rxjs/operators";
import { ShoppingListCategory } from "../../models/shopping-list-category";


@Injectable()
export class ProductService extends MyRestService<Product> {


    constructor(
        httpClient: HttpClient
    ) {
        super('products', httpClient);
    }

    getAllOfCategoryPagedSortedByName(category: ProductCategory): Observable<PagedResult<Product>> {
        const url = `${this.resourcePath}/search?categoryId=${category.id}`;

        return this.httpClient.get<PagedResult<Product>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }


    searchByCategoryAndNameAndSortByName(category: ProductCategory, name: string) {
        const url = `${this.resourcePath}/search?name=${name}&categoryId=${category.id}`;

        return this.httpClient.get<PagedResult<Product>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }


    searchByNameAndShoppingListCategory(name: string, category: ShoppingListCategory, includeUserProducts: boolean = false): Observable<PagedResult<Product>> {
        let url;
        if (includeUserProducts) {
            url = `${this.resourcePath}/search?name=${name}&shoppingListCategoryId=${category.id}&includeUserProducts=true`;
        } else {
            url = `${this.resourcePath}/search?name=${name}&shoppingListCategoryId=${category.id}`;
        }

        return this.httpClient.get<PagedResult<Product>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }
}
