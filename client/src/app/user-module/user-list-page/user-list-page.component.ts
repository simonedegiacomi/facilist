import { Component, OnInit } from '@angular/core';
import { ShoppingList } from "../../core-module/models/shopping-list";
import { ShoppingListService } from "../../core-module/services/rest/shopping-list.service";
import { ActivatedRoute, Router } from "@angular/router";

const $ = window['jQuery'];

@Component({
    templateUrl: './user-list-page.component.html',
    styleUrls: ['./user-list-page.component.css']
})
export class UserListPageComponent implements OnInit {

    list: ShoppingList;
    isChatOpen: Boolean = false;

    constructor(
        private shoppingListService: ShoppingListService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.fetchShoppingList();
    }

    fetchShoppingList () {
        this.shoppingListService.getById(this.shoppingListId)
            .subscribe(
                list => this.list = list,
                _ => this.router.navigateByUrl('/')
            );
    }

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
