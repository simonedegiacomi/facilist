import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { ShoppingList } from "../../core-module/models/shopping-list";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

    list$: Observable<ShoppingList>;

    constructor(
        private route: ActivatedRoute,
        private listService: ShoppingListService,
    ) {
    }

    ngOnInit() {
        this.fetchShoppingList();
    }

    fetchShoppingList () {
        this.list$ = this.listService.getById(this.shoppingListId)
    }

    get shoppingListId () { return this.route.snapshot.params.id; }

}
