import { Component, Input } from '@angular/core';
import { ShoppingListProduct } from "../../../models/shopping-list";
import { Subject } from "rxjs";
import { debounceTime, switchMap } from "rxjs/operators";
import { ShoppingListService } from "../../../services/shopping-list.service";

@Component({
    selector: 'user-shopping-list-product',
    templateUrl: './user-shopping-list-product.component.html',
    styleUrls: ['./user-shopping-list-product.component.css']
})
export class UserShoppingListProductComponent {

    @Input() product: ShoppingListProduct;


    sendEdits: Subject<null> = new Subject();


    constructor(
        private listService: ShoppingListService,
    ) {
        this.sendEdits.pipe(
            debounceTime(1500),
            switchMap(() => this.listService.updateProductInShoppingList(this.product))
        ).subscribe(updatedList => {

        });
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

    notifyChange() {
        this.sendEdits.next();
    }

}
