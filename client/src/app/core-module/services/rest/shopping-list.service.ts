import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { ShoppingList, ShoppingListPreview, ShoppingListProduct } from "../../models/shopping-list";
import { Product } from "../../models/product";

@Injectable()
export abstract class ShoppingListService {

    abstract getMyShoppingLists(): Observable<ShoppingListPreview[]>;

    abstract create(list: ShoppingList): Observable<ShoppingList>;

    abstract addProduct(list: ShoppingList, product: Product): Observable<ShoppingListProduct>;

    abstract updateProductInShoppingList(relation: ShoppingListProduct): Observable<ShoppingListProduct>;

    abstract delete(list: ShoppingList): Observable<any>;

    abstract update(list: ShoppingList): Observable<ShoppingList>;

    abstract getById(entityId: number): Observable<ShoppingList>;
}
