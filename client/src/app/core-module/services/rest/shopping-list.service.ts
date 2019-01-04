import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { ShoppingList, ShoppingListPreview, ShoppingListProduct } from "../../models/shopping-list";
import { Product } from "../../models/product";

/**
 * We define an interface for the service of the ShoppingList so we can have two implementations:
 * - backend implementation
 * - Local storage implementation (for the try now feature)
 */
@Injectable()
export abstract class ShoppingListService {

    abstract getMyShoppingLists(): Observable<ShoppingListPreview[]>;

    abstract create(list: ShoppingList): Observable<ShoppingList>;

    abstract addProduct(list: ShoppingList, product: Product): Observable<ShoppingListProduct>;

    abstract updateProductInShoppingList(relation: ShoppingListProduct): Observable<ShoppingListProduct>;

    abstract delete(list: ShoppingList): Observable<any>;

    abstract update(list: ShoppingList): Observable<ShoppingList>;

    abstract getById(entityId: number): Observable<ShoppingList>;

    abstract deleteProductFromShoppingList(product: ShoppingListProduct): Observable<any>;
}
