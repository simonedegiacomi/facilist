import { Component, Input, OnInit } from '@angular/core';
import { CollaborationsRoles, ShoppingList } from "../../../core-module/models/shopping-list";
import { Subject } from "rxjs";
import { AuthService } from "../../../core-module/services/auth.service";
import { ShoppingListSyncService } from "../../../core-module/services/sync/shopping-list-sync.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

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
            .subscribe(editedCollaboration => {
                const index = this.list.collaborations.findIndex(c => c.id == editedCollaboration.id);

                if (index >= 0) {
                    this.list.collaborations.splice(index, 1, editedCollaboration);
                }
            });

        this.shoppingListSyncService.collaborationDeleted(this.list)
            .subscribe(deletedCollaboration => {
                const index = this.list.collaborations.findIndex(c => c.id == deletedCollaboration.id);

                if (index >= 0) {
                    this.list.collaborations.splice(index, 1);
                }
            });
    }

    get isUserTheCreator() {
        return this.authService.user.id == this.list.creator.id
    }
}
