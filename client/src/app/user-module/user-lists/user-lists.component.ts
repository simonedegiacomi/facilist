import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { ShoppingListPreview, ShoppingListService } from "../../core-module/services/shopping-list.service";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit {

    lists$: Observable<ShoppingListPreview[]>;


    constructor(
        private listService: ShoppingListService
    ) {
        this.lists$ = this.listService.getMyShoppingLists()
    }

    ngOnInit() {

    }
}
