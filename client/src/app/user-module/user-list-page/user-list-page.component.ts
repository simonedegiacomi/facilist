import { Component, OnInit } from '@angular/core';
import { ShoppingList } from "../../core-module/models/shopping-list";
import { ShoppingListService } from "../../core-module/services/rest/shopping-list.service";
import { ActivatedRoute, Router } from "@angular/router";

const $ = window['jQuery'];

/**
 * Component that shows a shopping list and handles the sharing, chat and options of it.
 * This component shows the shopping list whose id is in the current url.
 */
@Component({
    templateUrl: './user-list-page.component.html',
    styleUrls: ['./user-list-page.component.css']
})
export class UserListPageComponent implements OnInit {

    /**
     * List currently shown
     */
    list: ShoppingList;

    /**
     * Wether or not the chat is currently opened
     */
    isChatOpen: Boolean = false;

    constructor(
        private shoppingListService: ShoppingListService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.fetchShoppingList();
    }

    /**
     * Load the shopping list from the server
     */
    fetchShoppingList () {
        this.shoppingListService.getById(this.shoppingListId)
            .subscribe(
                list => this.list = list,
                _ => this.router.navigateByUrl('/user')
            );
    }

    /**
     * Get the shopping list id from the url
     */
    get shoppingListId() {
        return this.route.snapshot.params.id;
    }

    onBack() {
        this.router.navigateByUrl('/user');
    }

    onOpenShareSettings () {
        $('#listSharingSettingsModal').modal('show');
    }

    onOpenSettings () {
        $('#listOptionsModal').modal('show');
    }

    onOpenChat() {
        this.isChatOpen = !this.isChatOpen;
    }
}
