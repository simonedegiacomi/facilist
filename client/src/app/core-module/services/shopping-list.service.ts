import { Injectable } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { ShoppingList, ShoppingListCollaboration } from "../models/shopping-list";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ShoppingListService extends MyRestService<ShoppingList> {

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

    updateProducts(list: ShoppingList): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/products`;

        return this.httpClient.post<ShoppingList>(
            url,
            list.products.map(product => {
                return {
                    productId:  product.product.id,
                    image:      product.image,
                    toBuy:      product.toBuy,
                    quantity:   product.quantity,
                    note:       product.note
                }
            })
        );
    }

    updateCollaborations(list: ShoppingList): Observable<ShoppingList>  {
        const url = `${this.resourcePath}/${list.id}/collaborations`;
        return this.httpClient.post<ShoppingList>(
            url,
            list.collaborations.map(collaboration => {
                return {
                    collaborationId: collaboration.id,
                    role: collaboration.role
                }
            })
        )
    }

    addCollaboratorByEmail(list: ShoppingList, email: string): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/collaborations`;
        return this.httpClient.put<ShoppingList>(url, { email })
    }

    deleteCollaboration (list: ShoppingList, toDelete: ShoppingListCollaboration): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/collaborations/${toDelete.id}`;
        return this.httpClient.delete<ShoppingList>(url)
    }
}

export class ShoppingListPreview {
    name: string;
    description: string;
    icon: string;

    itemsCount: boolean;
    itemsToBuyCount: boolean;

    shared: boolean;
}
