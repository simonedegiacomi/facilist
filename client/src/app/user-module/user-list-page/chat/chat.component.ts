import { Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ChatService } from "../../../core-module/services/rest/chat.service";
import { ChatMessage } from "../../../core-module/models/chat-message";
import { PagedResult } from "../../../core-module/services/rest/MyRestService";
import { ChatSyncService } from "../../../core-module/services/sync/chat-sync.service";
import { AuthService } from "../../../core-module/services/auth.service";
import { NgForm } from "@angular/forms";

/**
 * Components that handle the chat of a shopping list.
 * Messages are loaded in pages from the server. When the chat is opened only the latest messages are shown. As the use
 * scroll, older messages will befetched.
 */
@Component({
    selector: 'list-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnChanges {

    @Input() list: ShoppingList;

    /**
     * Wheter or not the chat is open
     */
    @Input() isOpen = false;

    /**
     * Event emitter that emits boolean (that indicates if the chat is open or not)
     */
    @Output() isOpenChange = new EventEmitter<boolean>();

    /**
     * Last page of messages loaded.
     */
    private lastLoadedPage: PagedResult<ChatMessage>;

    messages: ChatMessage[] = [];

    /**
     * New message that the user is writing
     */
    newMessage: string;

    /**
     * Is sending the new message. User to disable the textarea
     */
    isSending = false;

    /**
     * Flag that indicates if we're fetching older messages. Use to show a message in the view.
     */
    loadingPreviousMessages = false;

    /**
     * Flag that indicates if the chat was already opened once. Used to scroll to the bottom (latest message) only the
     * first time
     */
    private wasAlreadyOpenOnce = false;

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

    ngOnChanges(): void {
        if (this.isOpen && !this.wasAlreadyOpenOnce) {
            this.wasAlreadyOpenOnce = true;
            this.scrollToBottom();
        }
    }


    private listenForNewMessages () {
        this.chatSync.newMessageOfShoppingList(this.list)
            .subscribe(message => {
                this.messages.push(message);
                this.scrollToBottom();
            })
    }

    /**
     * Fetches latest messages
     */
    private fetchMessages () {
        this.loadingPreviousMessages = true;
        this.chatService.getPagedMessagesOfShoppingList(this.list)
            .subscribe(page => this.onOlderMessagesLoaded(page));
    }

    private scrollToBottom () {
        setTimeout(() => document.querySelector('.messages')
            .scrollTo(0, document.querySelector('.messages').scrollHeight));
    }

    /**
     * Called when the user scroll up to the top.
     * IF there are older messages, a new request to fetch them will be made
     */
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

    /**
     * Called every time the user presses a key and the textarea of the new message is focused. If the user presses the enter
     * button, the new message will be sent.
     * @param event
     */
    onKeyDown (event: KeyboardEvent) {
        if (event.keyCode == 13) {
            if (this.form.valid) {
                this.sendMessage();
            }
            return false;
        }

        return true;
    }

    /**
     * Closes the chat
     */
    closeChat() {
        this.isOpen = false;
        this.isOpenChange.emit(false);
    }
}
