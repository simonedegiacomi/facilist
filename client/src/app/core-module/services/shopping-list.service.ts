import { Injectable, Injector } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { ShoppingList } from "../models/shopping-list";
import { NetworkErrorsService } from "./network-errors.service";
import { ProductCategory } from "../models/product-category";

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService extends MyRestService<ShoppingList> {

    constructor(
        injector: Injector
    ) {
        super(ShoppingList, 'shoppingLists', injector);
    }

}
