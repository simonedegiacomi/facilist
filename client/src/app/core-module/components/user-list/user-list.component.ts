import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CollaborationsRoles, ShoppingList, ShoppingListProduct } from "../../models/shopping-list";
import { Product } from "../../models/product";
import { AuthService } from "../../services/auth.service";
import { ShoppingListSyncService } from "../../services/sync/shopping-list-sync.service";
import { ShoppingListService } from "../../services/rest/shopping-list.service";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

    @Input() list: ShoppingList;

    @Output() back              = new EventEmitter();
    @Output() openShareSettings = new EventEmitter();
    @Output() openSettings      = new EventEmitter();

    isSaving = false;

    lastUpdate: Date;

    constructor(
        private listService: ShoppingListService,
        private auth: AuthService,
        private shoppingListSyncService: ShoppingListSyncService
    ) {
    }

    ngOnInit() {
        this.listenForSyncUpdates();
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

        this.shoppingListSyncService.productInShoppingListDeleted(this.list)
            .subscribe(deletedProduct => {
                const index = this.list.products.findIndex(p => p.id == deletedProduct.id);
                this.list.products.splice(index, 1);
            });
    }


    onAddProduct(product: Product) {

        this.listService.addProduct(this.list, product).subscribe(() => {
            this.lastUpdate = new Date();
        });

    }

    removeProduct(product: ShoppingListProduct) {
        this.list.products.splice(this.list.products.indexOf(product), 1);
        //this.notifyChange();
    }

    get isDemoList ():boolean {
        return this.auth.user == null && this.list.creator == null;
    }

    get userCanEditCollaborations() {
        if (this.isDemoList) {
            return true;
        }

        const userId = this.auth.user.id;
        if (userId == this.list.creator.id) {
            return true;
        }

        const userCollaborations = this.list.collaborations.find(c => c.user.id == userId);
        return userCollaborations.role == CollaborationsRoles.SOCIAL ||
            userCollaborations.role == CollaborationsRoles.ADMIN;
    }

    get userCanEditInfo() {
        if (this.isDemoList) {
            return true;
        }

        const userId = this.auth.user.id;
        if (userId == this.list.creator.id) {
            return true;
        }

        const userCollaborations = this.list.collaborations.find(c => c.user.id == userId);
        return userCollaborations.role == CollaborationsRoles.ADMIN;
    }

}
