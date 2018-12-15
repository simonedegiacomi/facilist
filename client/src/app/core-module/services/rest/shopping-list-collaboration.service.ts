import { Injectable } from '@angular/core';
import { Invite, ShoppingList, ShoppingListCollaboration } from "../../models/shopping-list";
import { Observable } from "rxjs";
import { MyRestService } from "./MyRestService";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class ShoppingListCollaborationService extends MyRestService<ShoppingListCollaboration> {


    constructor(httpClient: HttpClient) {
        super('shoppingLists', httpClient);
    }

    updateCollaborations(list: ShoppingList): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/collaborations`;
        return this.httpClient.post<ShoppingList>(
            url,
            list.collaborations.map(collaboration => {
                return {
                    collaborationId: collaboration.id,
                    role: collaboration.role
                }
            })
        )
    }

    addCollaboratorByEmail(list: ShoppingList, email: string): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/collaborations`;
        return this.httpClient.put<ShoppingList>(url, email)
    }

    deleteCollaboration(list: ShoppingList, toDelete: ShoppingListCollaboration): Observable<ShoppingList> {
        const url = `${this.resourcePath}/${list.id}/collaborations/${toDelete.id}`;
        return this.httpClient.delete<ShoppingList>(url)
    }

    deleteInvite(list: ShoppingList, toDelete: Invite): Observable<void> {
        const url = `${this.resourcePath}/${list.id}/invites/${toDelete.id}`;
        return this.httpClient.delete<void>(url);
    }

}
