import { Component, Input } from '@angular/core';
import { ShoppingList, ShoppingListProduct } from "../../../models/shopping-list";
import { Subject } from "rxjs";
import { switchMap } from "rxjs/operators";
import { ShoppingListService } from "../../../services/rest/shopping-list.service";

/**
 * Show a product in a shopping list.
 * When the product in th elist it's updated, this component send the update request
 */
@Component({
    selector: 'user-shopping-list-product',
    templateUrl: './user-shopping-list-product.component.html',
    styleUrls: ['./user-shopping-list-product.component.css']
})
export class UserShoppingListProductComponent {

    /**
     * Product in a shopping list
     */
    @Input() product: ShoppingListProduct;

    @Input() list: ShoppingList;

    /**
     * Whether the product is in a demo list (used to disable the edit of the image)
     */
    @Input() isDemoListProduct: boolean;

    sendEdits: Subject<null> = new Subject();

    deleted = false;

    constructor(
        private listService: ShoppingListService,
    ) {
        this.sendEdits.pipe(
            switchMap(() => this.listService.updateProductInShoppingList(this.product))
        ).subscribe(_ => {});
    }

    toggleToBuy(product: ShoppingListProduct) {
        product.bought = !product.bought;
        this.notifyChange();
    }

    removeProduct(product: ShoppingListProduct) {
        this.deleted = true;
        this.listService.deleteProductFromShoppingList(this.list, product).subscribe(() => {});
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

}
