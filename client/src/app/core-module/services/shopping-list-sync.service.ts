import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import { ShoppingList } from "../models/shopping-list";
import { filter, map } from "rxjs/operators";
import { Observable } from "rxjs";
import { EventTypes, MySyncService } from "./MySyncService";
import { ShoppingListPreview } from "./shopping-list.service";

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

    shoppingListDeleted <T>(list: ShoppingList | ShoppingListPreview): Observable<ShoppingList | ShoppingListPreview> {
        return this.subscribe(`/topic/shoppingLists/${list.id}`).pipe(
            filter(event => event.event == EventTypes.DELETED),
            map(_ => list)
        );
    }


}
