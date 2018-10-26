import { Component, OnInit } from '@angular/core';
import { ShoppingListPreview, ShoppingListService } from "../../core-module/services/shopping-list.service";
import { RealtimeUpdatesService } from "../../core-module/services/realtime-updates.service";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit {

    lists: ShoppingListPreview[];

    constructor(
        private listService: ShoppingListService,
        private realtime: RealtimeUpdatesService
    ) { }

    ngOnInit() {
        this.fetchMyShoppingLists();
        this.listenForRealtimeUpdates();
    }

    fetchMyShoppingLists () {
        this.listService.getMyShoppingLists().subscribe(lists => this.lists = lists);
    }

    listenForRealtimeUpdates () {
        this.realtime.forShoppingLists().subscribe(list => this.lists.push(new ShoppingListPreview(list)))
    }
}
