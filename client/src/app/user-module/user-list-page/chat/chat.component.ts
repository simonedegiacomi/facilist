import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ChatService } from "../../../core-module/services/rest/chat.service";
import { ChatMessage } from "../../../core-module/models/chat-message";
import { PagedResult } from "../../../core-module/services/rest/MyRestService";
import { ChatSyncService } from "../../../core-module/services/sync/chat-sync.service";
import { AuthService } from "../../../core-module/services/auth.service";
import { NgForm } from "@angular/forms";

const $ = window['jQuery'];

@Component({
    selector: 'list-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

    @Input() list: ShoppingList;

    isOpen = false;

    private lastLoadedPage: PagedResult<ChatMessage>;

    messages: ChatMessage[] = [];

    newMessage: string;

    isSending = false;

    loadingPreviousMessages = false;

    @ViewChild('form') form: NgForm;

    constructor(
        private chatService: ChatService,
        private chatSync: ChatSyncService,
        private authService: AuthService
    ) { }

    get user () { return this.authService.user; }

    ngOnInit() {
        this.listenForNewMessages();
        this.fetchMessages();
    }

    private listenForNewMessages () {
        this.chatSync.newMessageOfShoppingList(this.list)
            .subscribe(message => {
                this.messages.push(message);
                this.scrollToBottom();
            })
    }

    private fetchMessages () {
        this.loadingPreviousMessages = true;
        this.chatService.getPagedMessagesOfShoppingList(this.list)
            .subscribe(page => this.onOlderMessagesLoaded(page));
    }

    toggleChat () {
        this.isOpen = !this.isOpen;
        // TODO: Update last seen message

        this.scrollToBottom();
    }

    private scrollToBottom () {
        setTimeout(() => document.querySelector('.messages')
            .scrollTo(0, document.querySelector('.smartphone').clientHeight));
    }

    onScrollUp () {
        if (this.lastLoadedPage.hasNext) {
            this.loadingPreviousMessages = true;
            this.chatService.getPagedMessagesOfShoppingListSentBeforeMessage(this.list, this.messages[0])
                .subscribe(page => this.onOlderMessagesLoaded(page))
        }
    }

    private onOlderMessagesLoaded (page:  PagedResult<ChatMessage>) {
        this.loadingPreviousMessages = false;
        this.lastLoadedPage = page;
        this.messages.splice(0, 0, ...page.content.reverse());
    }

    sendMessage () {
        this.isSending = true;
        this.chatService.sendMessage(this.list, this.newMessage).subscribe(() => this.isSending = false);

        this.newMessage = "";
    }

    onKeyDown (event: KeyboardEvent) {
        if (event.keyCode == 13 && !event.shiftKey) {
            if (this.form.valid) {
                this.sendMessage();
            }
            return false;
        }

        return true;
    }
}
