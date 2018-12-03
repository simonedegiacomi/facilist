import { Component, Input } from '@angular/core';
import { CollaborationsRoles, ShoppingList } from "../../../../core-module/models/shopping-list";

@Component({
    selector: 'share-collaborators',
    templateUrl: './share-collaborators.component.html',
    styleUrls: ['./share-collaborators.component.css']
})
export class ShareCollaboratorsComponent {

    @Input()
    list: ShoppingList;

    roles = CollaborationsRoles;

    constructor() {
    }

}
