import { Injectable } from '@angular/core';
import { StompService } from "@stomp/ng2-stompjs";
import { EventTypes, MySyncService } from "./MySyncService";
import { ShoppingList } from "../../models/shopping-list";
import { Observable } from "rxjs";
import { filter, map } from "rxjs/operators";
import { ChatMessage } from "../../models/chat-message";

@Injectable({
    providedIn: 'root'
})
export class ChatSyncService extends MySyncService {


    constructor(
        stompService: StompService
    ) {
        super(stompService);
    }

    newMessageOfShoppingList(list: ShoppingList): Observable<ChatMessage> {
        return this.subscribe<ChatMessage>(`/topic/shoppingLists/${list.id}/chat/messages`).pipe(
            filter(event => event.event == EventTypes.CREATED),
            map(event => event.model)
        )
    }

}
