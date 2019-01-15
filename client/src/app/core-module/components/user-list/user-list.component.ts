import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { ShoppingList } from "../../models/shopping-list";
import { Product } from "../../models/product";
import { AuthService } from "../../services/auth.service";
import { ShoppingListSyncService } from "../../services/sync/shopping-list-sync.service";
import { ShoppingListService } from "../../services/rest/shopping-list.service";
import { NotebookSheetButton } from "../notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

/**
 * Component that shows a shopping list, products in it and allow the user to add new products
 */
@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css'],
    encapsulation : ViewEncapsulation.None
})
export class UserListComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'Impostazioni di condivisione',
        iconClass: 'share-icon',
        onClick: () => this.openShareSettings.emit()
    }, {
        title: 'Impostazioni',
        iconClass: 'settings-icon',
        onClick: () => this.openSettings.emit()
    },  {
        title: 'Apri chat',
        iconClass: 'message-icon',
        onClick: () => this.openChat.emit(),
        shouldBeVisible: () => this.list.collaborations.length >= 1
    }, {
        title: 'Chiudi',
        iconClass: 'close-icon',
        onClick: () => this.back.emit()
    }];

    @Input() list: ShoppingList;
    @Input() allowNewProducts: boolean = true;

    @Output() back              = new EventEmitter();
    @Output() openShareSettings = new EventEmitter();
    @Output() openSettings      = new EventEmitter();
    @Output() openChat          = new EventEmitter();

    lastUpdate: Date;

    customProductName: string;

    constructor(
        private listService: ShoppingListService,
        private auth: AuthService,
        private shoppingListSyncService: ShoppingListSyncService
    ) { }

    ngOnInit() {
        this.listenForSyncUpdates();
    }

    /**
     * Listen for list updates from the server to keep the view always up to date
     */
    private listenForSyncUpdates() {
        // List for edits on list info (name, icon and description)
        this.shoppingListSyncService.shoppingListInfoEdited(this.list)
            .subscribe(edited => {
                this.list.name        = edited.name;
                this.list.description = edited.description;
            });

        // Listen for products added
        this.shoppingListSyncService.newProductInShoppingList(this.list)
            .subscribe(p => this.list.products.push(p));

        // Listen for products updated
        this.shoppingListSyncService.productInShoppingListEdited(this.list)
            .subscribe(updatedProduct => {
                const product = this.list.products.find(p => p.id == updatedProduct.id)

                product.note     = updatedProduct.note;
                product.image    = updatedProduct.image;
                product.quantity = updatedProduct.quantity;
                product.bought   = updatedProduct.bought;
            });

        // Listen for products deleted
        this.shoppingListSyncService.productInShoppingListDeleted(this.list)
            .subscribe(deletedProduct => {
                const index = this.list.products.findIndex(p => p.id == deletedProduct.id);
                this.list.products.splice(index, 1);
            });
    }

    /**
     * Sends the request to add a new product to the list
     * @param product Product to add
     */
    onAddProduct(product: Product) {
        this.listService.addProduct(this.list, product).subscribe(() => {
            this.lastUpdate = new Date();
        });
    }

    /**
     * Checks if the list is currently used in the try now feature
     */
    get isDemoList(): boolean {
        return this.auth.user == null && this.list.creator == null;
    }

    onCreateProduct(name: string) {
        this.customProductName = name;
        $('#newUserProductModal').modal('show');
    }

    /**
     * Checks if the list has at leas one product
     */
    get listHasProducts(): boolean { return this.list.products.length > 0; }
}
