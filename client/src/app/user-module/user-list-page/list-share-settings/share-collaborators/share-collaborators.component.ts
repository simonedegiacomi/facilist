import { Component, Input, OnChanges, OnInit } from '@angular/core';
import {
    CollaborationsRoles,
    ShoppingList,
    ShoppingListCollaboration
} from "../../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../../core-module/services/rest/shopping-list.service";

const $ = window['jQuery'];

@Component({
    selector: 'share-collaborators-and-invites',
    templateUrl: './share-collaborators.component.html',
    styleUrls: ['./share-collaborators.component.css']
})
export class ShareCollaboratorsComponent implements OnChanges {

    isSaving = false;

    @Input()
    list: ShoppingList;

    roles = CollaborationsRoles;

    constructor (
        private listService: ShoppingListService
    ) {}

    onDeleteCollaboration(toDelete: ShoppingListCollaboration) {
        this.isSaving = true;
        // TODO: Remove comment
        /*this.listService.deleteCollaboration(this.list, toDelete)
            .subscribe(_ => this.isSaving = false);*/
    }
    onDeleteInvite(email: string) {
        // TODO: Implement
    }


}
