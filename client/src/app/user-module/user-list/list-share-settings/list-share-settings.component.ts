import { Component, Input, OnInit } from '@angular/core';
import {
    CollaborationsRoles,
    ShoppingList,
    ShoppingListCollaboration
} from "../../../core-module/models/shopping-list";
import { Subject } from "rxjs";
import { debounceTime, switchMap, tap } from "rxjs/operators";
import { ShoppingListService } from "../../../core-module/services/shopping-list.service";

@Component({
    selector: 'user-list-share-settings',
    templateUrl: './list-share-settings.component.html',
    styleUrls: ['./list-share-settings.component.css']
})
export class ListShareSettingsComponent implements OnInit {

    @Input() list: ShoppingList;

    roles = CollaborationsRoles;

    sendEdits: Subject<null> = new Subject();

    isSaving         = false;
    lastUpdate: Date = null;

    newCollaborator: string;

    constructor(
        private listService: ShoppingListService
    ) {

    }

    ngOnInit() {
        this.setUpSendEdits();
    }

    private setUpSendEdits() {
        this.sendEdits.pipe(
            debounceTime(3000),
            tap(() => this.isSaving = true),
            switchMap(() => this.listService.updateCollaborations(this.list))
        ).subscribe(updatedList => {
            this.lastUpdate = new Date();
            this.isSaving   = false;
        });
    }

    notifyChanges() {
        this.sendEdits.next();
    }

    addCollaborator () {
        this.listService.addCollaboratorByEmail(this.list, this.newCollaborator)
            .subscribe(updatedList => console.log(updatedList));
        // TODO: Update UI
    }

    onDeleteCollaboration (toDelete: ShoppingListCollaboration) {
        this.listService.deleteCollaboration(this.list, toDelete)
            .subscribe(updatedList => console.log(updatedList));
        // TODO: Update UI
    }

}
