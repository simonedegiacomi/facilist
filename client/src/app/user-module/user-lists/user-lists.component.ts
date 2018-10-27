import { Component, OnDestroy, OnInit } from '@angular/core';
import { ShoppingListPreview, ShoppingListService } from "../../core-module/services/shopping-list.service";
import { Subscription } from "rxjs";
import { ShoppingListSyncService } from "../../core-module/services/shopping-list-sync.service";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit, OnDestroy {

    lists: ShoppingListPreview[];

    private subscriptions: Subscription[] = [];

    constructor(
        private listService: ShoppingListService,
        private syncService: ShoppingListSyncService
    ) {
    }

    ngOnInit() {
        this.fetchMyShoppingLists();
        this.listenForSyncUpdatesOfNewLists();
    }

    fetchMyShoppingLists() {
        this.listService.getMyShoppingLists()
            .subscribe(lists => {
                this.lists = lists;
                lists.forEach(list => this.listenForUpdatesOfList(list))
            });
    }

    listenForSyncUpdatesOfNewLists() {
        this.subscriptions.push(
            this.syncService.newShoppingList()
                .subscribe(list => {
                    this.lists.push(list);
                    this.listenForUpdatesOfList(list);
                })
        );
    }

    listenForUpdatesOfList(list: ShoppingListPreview) {

        this.subscriptions.push(
            this.syncService.shoppingListInfoEdited(list)
                .subscribe(list => {
                    const oldList = this.lists.find(l => l.id == list.id);

                    oldList.name             = list.name;
                    oldList.description      = list.description;
                    oldList.icon             = list.icon;
                    oldList.itemsCount       = list.itemsCount;
                    oldList.boughtItemsCount = list.boughtItemsCount;
                    oldList.shared           = list.shared;
                })
        );

        this.subscriptions.push(
            this.syncService.shoppingListDeleted(list)
                .subscribe(_ => this.lists.splice(this.lists.indexOf(list), 1))
        );
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(s => s.unsubscribe());
        this.subscriptions = [];
    }
}
