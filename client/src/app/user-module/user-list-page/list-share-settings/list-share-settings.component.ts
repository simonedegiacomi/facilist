import { Component, Input, OnInit } from '@angular/core';
import {
    CollaborationsRoles,
    ShoppingList,
    ShoppingListCollaboration
} from "../../../core-module/models/shopping-list";
import { Subject } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap, tap } from "rxjs/operators";
import { AuthService } from "../../../core-module/services/auth.service";
import { ShoppingListCollaborationService } from "../../../core-module/services/rest/shopping-list-collaboration.service";
import { UserService } from "../../../core-module/services/rest/user.service";
import { User } from "../../../core-module/models/user";
import { ShoppingListSyncService } from "../../../core-module/services/sync/shopping-list-sync.service";

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


    private filter = new Subject<string>();

    suggestedUsers: User[] = [];

    constructor(
        private listService: ShoppingListCollaborationService,
        private authService: AuthService,
        private userService: UserService,
        private shoppingListSyncService: ShoppingListSyncService
    ) {

    }

    ngOnInit() {
        this.setUpSendEdits();
        this.setupSearch();
        this.listenForSyncUpdates();
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

    private setupSearch() {
        this.filter.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(email => this.userService.findUsersByEmail(email))
        ).subscribe(users => this.suggestedUsers = users);
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

    onUpdateSearchFilter(searchFilter: string) {
        this.filter.next(searchFilter);
    }

    notifyChanges() {
        this.sendEdits.next();
    }

    addCollaborator() {
        this.isSaving = true;
        this.listService.addCollaboratorByEmail(this.list, this.newCollaborator)
            .subscribe(_ => {
                this.isSaving = false;
                this.newCollaborator = "";
            });
    }

    onDeleteCollaboration(toDelete: ShoppingListCollaboration) {
        this.isSaving = true;
        this.listService.deleteCollaboration(this.list, toDelete)
            .subscribe(_ => this.isSaving = false);
    }

    get isUserTheCreator() {
        return this.authService.user.id == this.list.creator.id
    }

    isUserByEmailCollaborating(email: string) {
        return this.list.creator.email == email ||
            this.list.collaborations.map(c => c.user).find(u => u.email == email) != null;
    }
}
