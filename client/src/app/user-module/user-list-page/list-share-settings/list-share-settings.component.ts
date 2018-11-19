import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
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
        private userService: UserService
    ) {

    }

    ngOnInit() {
        this.setUpSendEdits();
        this.setupSearch();
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

    private setupSearch () {
        this.filter.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(email => this.userService.findUsersByEmail(email))
        ).subscribe(users => this.suggestedUsers = users);
    }

    onUpdateSearchFilter (searchFilter: string) {
        this.filter.next(searchFilter);
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

    get isUserTheCreator () { return this.authService.user.id == this.list.creator.id }

    isUserByEmailCollaborating (email: string) {
        return this.list.collaborations.map(c => c.user).findIndex(u => u.email == email);
    }
}
