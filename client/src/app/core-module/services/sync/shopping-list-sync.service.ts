import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import {
    Invite,
    ShoppingList,
    ShoppingListCollaboration,
    ShoppingListPreview,
    ShoppingListProduct
} from "../../models/shopping-list";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";
import { EventTypes, MySyncService } from "./MySyncService";

const {CREATED, MODIFIED, DELETED} = EventTypes;

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
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}`, MODIFIED);
    }

    shoppingListDeleted(list: ShoppingList | ShoppingListPreview): Observable<ShoppingList | ShoppingListPreview> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}`, DELETED);
    }

    newProductInShoppingList(list: ShoppingList): Observable<ShoppingListProduct> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/products`, CREATED);
    }


    productInShoppingListEdited(list: ShoppingList): Observable<ShoppingListProduct> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/products`, MODIFIED);
    }


    productInShoppingListDeleted(list: ShoppingList): Observable<ShoppingListProduct> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/products`, DELETED);
    }

    newCollaboration(list: ShoppingList): Observable<ShoppingListCollaboration> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/collaborations`, CREATED);
    }

    collaborationEdited(list: ShoppingList): Observable<ShoppingListCollaboration> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/collaborations`, MODIFIED);
    }

    collaborationDeleted(list: ShoppingList): Observable<ShoppingListCollaboration> {
        return this.subscribeByEventTypeAndMapModel(`/topic/shoppingLists/${list.id}/collaborations`, DELETED);
    }

    newInvite(list: ShoppingList): Observable<Invite> {
        return this.subscribeByEventTypeAndMapModel<Invite>(`/topic/shoppingLists/${list.id}/invites`, CREATED);
    }

    inviteDeleted(list: ShoppingList): Observable<Invite> {
        return this.subscribeByEventTypeAndMapModel<Invite>(`/topic/shoppingLists/${list.id}/invites`, DELETED);
    }

}
