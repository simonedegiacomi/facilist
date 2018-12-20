import { Component, Input, OnInit } from '@angular/core';
import { ShoppingList } from "../../../../core-module/models/shopping-list";
import { Subject } from "rxjs";
import { User } from "../../../../core-module/models/user";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";
import { UserService } from "../../../../core-module/services/rest/user.service";
import { ShoppingListCollaborationService } from "../../../../core-module/services/rest/shopping-list-collaboration.service";

@Component({
    selector: 'add-collaborator',
    templateUrl: './add-collaborator.component.html',
    styleUrls: ['./add-collaborator.component.css']
})
export class AddCollaboratorComponent implements OnInit {

    isSaving = false;

    newCollaborator: string;

    @Input()
    list: ShoppingList;

    private filter = new Subject<string>();

    suggestedUsers: User[] = [];

    constructor(
        private userService: UserService,
        private listService: ShoppingListCollaborationService
    ) {
    }

    ngOnInit() {
        this.setupSearch();
    }

    private setupSearch() {
        this.filter.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(email => this.userService.findUsersByEmail(email))
        ).subscribe(users => this.suggestedUsers = users);
    }

    onUpdateSearchFilter(searchFilter: string) {
        this.filter.next(searchFilter);
    }

    isUserByEmailCollaborating(email: string) {
        return this.list.creator.email == email ||
            this.list.collaborations.map(c => c.user).find(u => u.email == email) != null;
    }

    addCollaborator(email: string) {
        if (this.isUserByEmailCollaborating(email)) {
            return;
        }

        this.isSaving = true;
        this.listService.addCollaboratorByEmail(this.list, email)
            .subscribe(_ => {
                this.isSaving = false;
                this.newCollaborator = "";
            });
    }
}
