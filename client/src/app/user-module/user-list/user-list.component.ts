import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ActivatedRoute } from "@angular/router";
import { Subject } from "rxjs";
import { CollaborationsRoles, ShoppingList, ShoppingListProduct } from "../../core-module/models/shopping-list";
import { debounceTime, switchMap, tap } from "rxjs/operators";
import { Product } from "../../core-module/models/product";
import { AuthService } from "../../core-module/services/auth.service";
import { RealtimeUpdatesService } from "../../core-module/services/realtime-updates.service";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

    list: ShoppingList;

    sendEdits: Subject<null> = new Subject();

    isSaving = false;

    lastUpdate: Date;

    productToAdd: Product;

    constructor(
        private route: ActivatedRoute,
        private listService: ShoppingListService,
        private auth: AuthService,
        private realtimeUpdatesService: RealtimeUpdatesService
    ) {
    }

    ngOnInit() {
        this.fetchShoppingList();
        this.setUpSendEdits();
    }

    private fetchShoppingList() {
        this.listService.getById(this.shoppingListId).subscribe(list => {
            this.list = list

            this.realtimeUpdatesService.forShoppingList(this.list).subscribe(u => {
                console.log('ok', u)
            })
        });
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
        product.bought = !product.bought;
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

    onAddProduct(product: Product) {
        this.list.products.push(new ShoppingListProduct(product));
        this.notifyChange();
    }

    removeProduct(product: ShoppingListProduct) {
        this.list.products.splice(this.list.products.indexOf(product), 1);
        this.notifyChange();
    }


    notifyChange() {
        this.sendEdits.next();
    }

    get userCanEditCollaborations() {
        const userId = this.auth.user.id;
        if (userId == this.list.creator.id) {
            return true;
        }

        const userCollaborations = this.list.collaborations.find(c => c.user.id == userId);
        return userCollaborations.role == CollaborationsRoles.SOCIAL ||
            userCollaborations.role == CollaborationsRoles.SOCIAL;
    }

}
