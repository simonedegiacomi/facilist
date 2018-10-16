import { Product } from "../models/product";
import { Injectable, Injector } from "@angular/core";
import { Observable } from "rxjs";
import { ProductCategory } from "../models/product-category";
import { MyRestService, PagedResult, sortByName } from "./MyRestService";
import { HttpClient } from "@angular/common/http";


@Injectable()
export class ProductService extends MyRestService<Product> {


    constructor(
        httpClient: HttpClient
    ) {
        super('products', httpClient);
    }

    getAllOfCategoryPagedSortedByName(category: ProductCategory): Observable<PagedResult<Product>> {
        return this.paginate(this.search('findByCategory', {
            ...sortByName,
            params: [{
                key: 'category',
                value: category._links.self.href
            }]
        }));
    }


    searchByCategoryAndNameAndSortByName(category: ProductCategory, name: string) {
        return this.paginate(this.search('findByCategoryAndNameContainingIgnoreCase', {
            ...sortByName,
            params: [{
                key: 'name',
                value: name
            }, {
                key: 'category',
                value: category._links.self.href
            }]
        }));
    }


}
