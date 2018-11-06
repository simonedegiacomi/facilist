import { Component, Input, OnInit } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ChatService } from "../../../core-module/services/rest/chat.service";
import { ChatMessage } from "../../../core-module/models/chat-message";
import { PagedResult } from "../../../core-module/services/rest/MyRestService";
import { ChatSyncService } from "../../../core-module/services/sync/chat-sync.service";

@Component({
    selector: 'list-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

    @Input() list: ShoppingList;

    isOpen = false;

    private lastPage: PagedResult<ChatMessage>;

    messages: ChatMessage[] = [];

    newMessage: string;

    isSending = false;

    constructor(
        private chatService: ChatService,
        private chatSync: ChatSyncService
    ) {
    }

    ngOnInit() {
        this.listenForNewMessages();
        this.fetchMessages();
    }

    private listenForNewMessages () {
        this.chatSync.newMessageOfShoppingList(this.list)
            .subscribe(message => this.messages.push(message))
    }

    private fetchMessages () {
        this.chatService.getPagedMessagesOfShoppingList(this.list).subscribe(page => {
            this.lastPage = page;
            this.messages = page.content.reverse();
        });
    }

    toggleChat () {
        this.isOpen = !this.isOpen;
    }

    onScrollUp () {
        console.log('scrolled up');
    }

    sendMessage () {
        this.isSending = true;
        this.chatService.sendMessage(this.list, this.newMessage).subscribe(() => {
            this.isSending = false;
            this.newMessage = "";
        });
    }
}
