import { Component, Input, OnInit } from '@angular/core';
import { CollaborationsRoles, ShoppingList } from "../../../core-module/models/shopping-list";
import { Subject } from "rxjs";
import { AuthService } from "../../../core-module/services/auth.service";
import { ShoppingListSyncService } from "../../../core-module/services/sync/shopping-list-sync.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

// TODO: Move utility somewhere else
function removeFromArrayIfPresent (array, item) {
    const index = array.indexOf(item);

    if (index >= 0) {
        array.splice(index, 1);
    }
}

// TODO: Move utility somewhere else
function removeFromArrayByIdIfPresent(array, id) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1);
    }
}

// TODO: Move utility somewhere else
function replaceArrayItemByIdIfPresent(array, id, newItem) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1, newItem);
    }
}

@Component({
    selector: 'user-list-share-settings',
    templateUrl: './list-share-settings.component.html',
    styleUrls: ['./list-share-settings.component.css']
})
export class ListShareSettingsComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => $('#listSharingSettingsModal').modal('hide')
    }];


    @Input() list: ShoppingList;

    roles = CollaborationsRoles;

    sendEdits: Subject<null> = new Subject();

    constructor(
        private authService: AuthService,
        private shoppingListSyncService: ShoppingListSyncService
    ) {

    }

    ngOnInit() {
        this.listenForSyncUpdates();
    }


    private listenForSyncUpdates () {
        this.shoppingListSyncService.newCollaboration(this.list)
            .subscribe(collaboration => this.list.collaborations.push(collaboration));

        this.shoppingListSyncService.collaborationEdited(this.list)
            .subscribe(edited => replaceArrayItemByIdIfPresent(this.list.collaborations, edited.id, edited));

        this.shoppingListSyncService.collaborationDeleted(this.list)
            .subscribe(deleted => removeFromArrayByIdIfPresent(this.list.collaborations, deleted.id));

        this.shoppingListSyncService.newInvite(this.list)
            .subscribe(invite => this.list.invites.push(invite));

        this.shoppingListSyncService.inviteDeleted(this.list)
            .subscribe(deleted => removeFromArrayByIdIfPresent(this.list.invites, deleted.id));

    }

    get isUserTheCreator() {
        return this.authService.user.id == this.list.creator.id
    }
}
