import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CollaborationsRoles, ShoppingList, ShoppingListProduct } from "../../models/shopping-list";
import { Product } from "../../models/product";
import { AuthService } from "../../services/auth.service";
import { ShoppingListSyncService } from "../../services/shopping-list-sync.service";
import { ShoppingListService } from "../../services/shopping-list.service";

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


    get userCanEditCollaborations() {
        // TODO: This is an hacky way to show the share button also for the demo list.
        if (this.auth.user == null && this.list.creator == null) {
            return true;
        }

        const userId = this.auth.user.id;
        if (userId == this.list.creator.id) {
            return true;
        }

        const userCollaborations = this.list.collaborations.find(c => c.user.id == userId);
        return userCollaborations.role == CollaborationsRoles.SOCIAL ||
            userCollaborations.role == CollaborationsRoles.SOCIAL;
    }

}
