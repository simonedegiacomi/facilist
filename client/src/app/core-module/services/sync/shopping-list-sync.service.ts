import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import { ShoppingList, ShoppingListPreview, ShoppingListProduct } from "../../models/shopping-list";
import { filter, map } from "rxjs/operators";
import { Observable } from "rxjs";
import { EventTypes, MySyncService } from "./MySyncService";

@Injectable({
    providedIn: 'root'
})
export class ShoppingListSyncService extends MySyncService {


    constructor(
        stompService: StompService
    ) {
        super(stompService);
    }

    newShoppingList(): Observable<ShoppingListPreview> {
        return this.subscribe<ShoppingListPreview>("/user/queue/shoppingLists").pipe(
            map(event => event.model)
        );
    }

    shoppingListInfoEdited(list: ShoppingList | ShoppingListPreview): Observable<ShoppingListPreview> {
        return this.subscribe<ShoppingListPreview>(`/topic/shoppingLists/${list.id}`).pipe(
            filter(event => event.event == EventTypes.MODIFIED),
            map(event => event.model)
        )
    }

    shoppingListDeleted (list: ShoppingList | ShoppingListPreview): Observable<ShoppingList | ShoppingListPreview> {
        return this.subscribe(`/topic/shoppingLists/${list.id}`).pipe(
            filter(event => event.event == EventTypes.DELETED),
            map(_ => list)
        );
    }

    newProductInShoppingList(list:ShoppingList): Observable<ShoppingListProduct> {
        return this.subscribe<ShoppingListProduct>(`/topic/shoppingLists/${list.id}/products`).pipe(
            filter(event => event.event == EventTypes.CREATED),
            map(event => event.model)
        );
    }


    productInShoppingListEdited(list: ShoppingList) : Observable<ShoppingListProduct> {
        return this.subscribe<ShoppingListProduct>(`/topic/shoppingLists/${list.id}/products`).pipe(
            filter(event => event.event == EventTypes.MODIFIED),
            map(event => event.model)
        );
    }


    productInShoppingListDeleted(list: ShoppingList) : Observable<ShoppingListProduct> {
        return this.subscribe<ShoppingListProduct>(`/topic/shoppingLists/${list.id}/products`).pipe(
            filter(event => event.event == EventTypes.DELETED),
            map(event => event.model)
        );
    }
}
