import { Injectable } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import {
    ShoppingList,
    ShoppingListCollaboration,
    ShoppingListPreview,
    ShoppingListProduct
} from "../../core-module/models/shopping-list";
import { Observable, of } from "rxjs";
import { Product } from "../../core-module/models/product";

@Injectable()
export class LocalStorageShoppingListService implements ShoppingListService {

    constructor() {
    }

    addCollaboratorByEmail(list: ShoppingList, email: string): Observable<ShoppingList> {
        throw "unsupported operation";
    }

    addProduct(list: ShoppingList, product: Product): Observable<ShoppingListProduct> {
        const relation = new ShoppingListProduct(product);
        relation.id    = new Date().getMilliseconds();

        list.products.push(relation);
        this.saveListToLocalStorage(list);

        return of(relation);
    }

    create(list: ShoppingList): Observable<ShoppingList> {
        return this.update(list);
    }

    deleteCollaboration(list: ShoppingList, toDelete: ShoppingListCollaboration): Observable<ShoppingList> {
        throw "unsupported operation";
    }

    getMyShoppingLists(): Observable<ShoppingListPreview[]> {
        const lists  = [];
        const stored = this.getListFromLocalStorageOrNull();

        if (stored != null) {
            lists.push(new ShoppingListPreview(stored));
        }

        return of(lists);
    }

    updateCollaborations(list: ShoppingList): Observable<ShoppingList> {
        throw "unsupported operation";
    }

    updateProductInShoppingList(updatedRel: ShoppingListProduct): Observable<ShoppingListProduct> {
        const list  = this.getListFromLocalStorageOrNull();
        const index = list.products.findIndex(rel => rel.id == updatedRel.id);

        list.products.splice(index, 1);
        list.products.push(updatedRel);

        this.saveListToLocalStorage(list);

        return of(updatedRel);
    }

    delete(list: ShoppingList): Observable<any> {
        this.clearLocalStorage();
        return of(null);
    }

    update(list: ShoppingList): Observable<ShoppingList> {
        this.saveListToLocalStorage(list);
        return of(list);
    }

    getById(entityId: number): Observable<ShoppingList> {
        return of(this.getListFromLocalStorageOrNull());
    }

    private getListFromLocalStorageOrNull(): ShoppingList {
        const stored = window.localStorage.getItem('demoList');

        if (stored == null) {
            return null;
        }

        return JSON.parse(stored);
    }


    private saveListToLocalStorage(list: ShoppingList) {
        window.localStorage.setItem('demoList', JSON.stringify(list));
    }

    private clearLocalStorage () {
        window.localStorage.clear();
    }

}
