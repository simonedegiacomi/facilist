import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import { ShoppingList } from "../models/shopping-list";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class RealtimeUpdatesService {

    constructor(
        private stompService: StompService
    ) {
        stompService.subscribe('/user').subscribe(m => console.log(m));
    }

    forShoppingList(list: ShoppingList) {
        return this.stompService.subscribe(`/shoppingLists/${list.id}`).pipe(
            map(m => JSON.parse(m.body))
        );
    }

    forShoppingLists(): Observable<ShoppingList> {
        return this.stompService.subscribe(`/user/queue/shoppingLists`).pipe(
            map(m => JSON.parse(m.body))
        );
    }
}
