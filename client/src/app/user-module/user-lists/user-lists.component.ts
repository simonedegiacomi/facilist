import { Component, OnInit } from '@angular/core';
import { ShoppingListPreview, ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ShoppingList } from "../../core-module/models/shopping-list";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit {

    lists: ShoppingListPreview[];

    constructor(
        private listService: ShoppingListService
    ) { }

    ngOnInit() {
        this.fetchMyShoppingLists()
    }

    fetchMyShoppingLists () {
        this.listService.getMyShoppingLists().subscribe(lists => this.lists = lists);
    }

    onNewListCreated (newList: ShoppingList) {
        this.lists.push(new ShoppingListPreview(newList));
    }
}
