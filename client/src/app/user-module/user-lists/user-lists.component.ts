import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from "rxjs";
import { ShoppingListSyncService } from "../../core-module/services/sync/shopping-list-sync.service";
import { ShoppingListPreview } from "../../core-module/models/shopping-list";
import { ShoppingListService } from "../../core-module/services/rest/shopping-list.service";
import { NearYouService } from "../../core-module/services/near-you.service";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit, OnDestroy {

    isNearYouEnabled = false;
    askingNearYouPermission = false;

    lists: ShoppingListPreview[];

    private subscriptions: Subscription[] = [];

    constructor(
        private listService: ShoppingListService,
        private syncService: ShoppingListSyncService,
        private nearYouService: NearYouService
    ) {
    }

    ngOnInit() {
        this.fetchMyShoppingLists();
        this.listenForSyncUpdatesOfNewLists();
        this.setupNearYouService();
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

    setupNearYouService () {
        this.nearYouService.hasGivenPermissionToUseGeolocation()
            .subscribe(enabled => {
                this.isNearYouEnabled = enabled;
                if (enabled) {
                    this.nearYouService.start();
                }
            })
    }

    enableNearYouService () {
        this.askingNearYouPermission = true;
        this.nearYouService.askPermission().subscribe(() => {
            this.isNearYouEnabled = true;
            this.askingNearYouPermission = false;
            this.nearYouService.start();
        });
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

    get hasLists (): boolean { return this.lists != null && this.lists.length > 0 }
}
