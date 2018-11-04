import { Injectable } from '@angular/core';
import { MyRestService } from "../../core-module/services/rest/MyRestService";
import { ShoppingList, ShoppingListPreview, ShoppingListProduct } from "../../core-module/models/shopping-list";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product } from "../../core-module/models/product";
import { ShoppingListService } from "../../core-module/services/rest/shopping-list.service";

@Injectable()
export class BackendShoppingListService extends MyRestService<ShoppingList> implements ShoppingListService {

    constructor(
        httpClient: HttpClient
    ) {
        super('shoppingLists', httpClient);
    }



    getMyShoppingLists(): Observable<ShoppingListPreview[]> {
        return this.httpClient.get<ShoppingListPreview[]>('/api/users/me/shoppingLists');
    }

    create(list: ShoppingList): Observable<ShoppingList> {
        const entity = {
            ...list,
            shoppingListCategoryId: list.category.id
        };

        return super.create(entity);
    }



    updateProductInShoppingList(relation: ShoppingListProduct): Observable<ShoppingListProduct> {
        return this.httpClient.put<ShoppingListProduct>(
            `/api/shoppingListProducts/${relation.id}`,
            relation
        );
    }

    addProduct(list: ShoppingList, product: Product): Observable<ShoppingListProduct> {
        return this.httpClient.post<ShoppingListProduct>(
            `${this.resourcePath}/${list.id}/products`,
            product.id
        )
    }

    deleteProductFromShoppingList(relation: ShoppingListProduct): Observable<any> {
        return this.httpClient.delete<ShoppingListProduct>(
            `/api/shoppingListProducts/${relation.id}`
        );
    }

}
