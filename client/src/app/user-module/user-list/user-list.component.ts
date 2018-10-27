import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ActivatedRoute } from "@angular/router";
import { CollaborationsRoles, ShoppingList, ShoppingListProduct } from "../../core-module/models/shopping-list";
import { Product } from "../../core-module/models/product";
import { AuthService } from "../../core-module/services/auth.service";
import { ShoppingListSyncService } from "../../core-module/services/shopping-list-sync.service";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

    list: ShoppingList;

    isSaving = false;

    lastUpdate: Date;

    constructor(
        private route: ActivatedRoute,
        private listService: ShoppingListService,
        private auth: AuthService,
        private shoppingListSyncService: ShoppingListSyncService
    ) {
    }

    ngOnInit() {
        this.fetchShoppingList();
    }

    private fetchShoppingList() {
        this.listService.getById(this.shoppingListId).subscribe(list => {
            this.list = list;
            this.listenForSyncUpdates();
        });
    }

    private listenForSyncUpdates() {
        this.shoppingListSyncService.newProductInShoppingList(this.list)
            .subscribe(p => this.list.products.push(p));

        this.shoppingListSyncService.productInShoppingListEdited(this.list)
            .subscribe(updatedProduct => {
                const product = this.list.products.find(p => p.id == updatedProduct.id)

                product.note     = updatedProduct.note;
                product.image    = updatedProduct.image;
                product.quantity = updatedProduct.quantity;
                product.bought   = updatedProduct.bought;
            });
    }

    get shoppingListId() {
        return this.route.snapshot.params.id;
    }

    onAddProduct(product: Product) {

        this.listService.addProduct(this.list, product).subscribe(() => {
            this.lastUpdate = new Date();
        });

    }

    removeProduct(product: ShoppingListProduct) {
        this.list.products.splice(this.list.products.indexOf(product), 1);
        this.notifyChange();
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
