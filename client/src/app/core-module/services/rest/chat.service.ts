import { Injectable } from '@angular/core';
import { MyRestService, PagedResult } from "./MyRestService";
import { ChatMessage } from "../../models/chat-message";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ShoppingList } from "../../models/shopping-list";

@Injectable({
    providedIn: 'root'
})
export class ChatService extends MyRestService<ChatMessage> {

    constructor(
        httpClient: HttpClient
    ) {
        super('', httpClient)
    }


    public getPagedMessagesOfShoppingList(
        list: ShoppingList,
        page: number = 0,
        size: number = 20
    ): Observable<PagedResult<ChatMessage>> {
        return this.getPaged(`/api/shoppingLists/${list.id}/chat/messages`, page, size);
    }

    public sendMessage(
        list: ShoppingList,
        message: string
    ): Observable<any> {
        return this.httpClient.post(`/api/shoppingLists/${list.id}/chat/messages`, message);
    }

    getPagedMessagesOfShoppingListSentBeforeMessage(
        list: ShoppingList,
        lastMessage: ChatMessage
    ): Observable<PagedResult<ChatMessage>> {
        return this.getPaged(`/api/shoppingLists/${list.id}/chat/messages?lastMessageId=${lastMessage.id}`);
    }
}
