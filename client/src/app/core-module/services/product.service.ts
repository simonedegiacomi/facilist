import { Product } from "../models/product";
import { Injectable, Injector } from "@angular/core";
import { Observable } from "rxjs";
import { ProductCategory } from "../models/product-category";
import { MyRestService, PagedResult, sortByName } from "./MyRestService";
import { HttpClient } from "@angular/common/http";
import { map } from "rxjs/operators";


@Injectable()
export class ProductService extends MyRestService<Product> {


    constructor(
        httpClient: HttpClient
    ) {
        super('products', httpClient);
    }

    getAllOfCategoryPagedSortedByName(category: ProductCategory): Observable<PagedResult<Product>> {
        const url = `${this.resourcePath}/search/byCategory?categoryId=${category.id}`;

        return this.httpClient.get<PagedResult<ProductCategory>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }


    searchByCategoryAndNameAndSortByName(category: ProductCategory, name: string) {
        const url = `${this.resourcePath}/search/byNameAndCategory?name=${name}&categoryId=${category.id}`;

        return this.httpClient.get<PagedResult<ProductCategory>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }


}
