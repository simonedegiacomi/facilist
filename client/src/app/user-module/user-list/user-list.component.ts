import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ActivatedRoute } from "@angular/router";
import { Subject } from "rxjs";
import { ShoppingList, ShoppingListProduct } from "../../core-module/models/shopping-list";
import { debounceTime, switchMap, tap } from "rxjs/operators";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

    list: ShoppingList;

    sendEdits: Subject<null> = new Subject();

    isSaving = false;
,
    lastUpdate: Date;

    constructor(
        private route: ActivatedRoute,
        private listService: ShoppingListService,
    ) {
    }

    ngOnInit() {
        this.fetchShoppingList();
        this.setUpSendEdits();
    }

    private fetchShoppingList() {
        this.listService.getById(this.shoppingListId).subscribe(list => this.list = list);
    }

    private setUpSendEdits() {
        this.sendEdits.pipe(
            debounceTime(3000),
            tap(() => this.isSaving = true),
            switchMap(() => this.listService.updateProducts(this.list))
        ).subscribe(updatedList => {
            this.lastUpdate = new Date();
            this.isSaving   = false;
        });
    }

    get shoppingListId() {
        return this.route.snapshot.params.id;
    }

    toggleToBuy(product: ShoppingListProduct) {
        product.toBuy = !product.toBuy;
        this.notifyChange();
    }

    decrementQuantity(product: ShoppingListProduct) {
        product.quantity--;
        this.notifyChange();
    }

    incrementQuantity(product: ShoppingListProduct) {
        product.quantity++;
        this.notifyChange();
    }

    notifyChange() {
        this.sendEdits.next();
    }

    /**
     * this.filter.pipe(
     debounceTime(300),
     distinctUntilChanged(),
     switchMap(filter => {
                if (this.selectedCategory) {
                    return this.productsService.searchByCategoryAndNameAndSortByName(this.selectedCategory, filter);
                } else {
                    return this.productsService.searchByNameAndSortByName(filter);
                }
            })
     ).subscribe(page => this.productsPage = page);
     */

}
