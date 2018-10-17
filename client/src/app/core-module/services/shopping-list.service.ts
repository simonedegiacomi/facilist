import { Injectable } from '@angular/core';
import { MyRestService } from "./MyRestService";
import { ShoppingList } from "../models/shopping-list";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService extends MyRestService<ShoppingList> {

    constructor(
        httpClient: HttpClient
    ) {
        super('shoppingLists', httpClient);
    }

}
