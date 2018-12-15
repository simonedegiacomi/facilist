import { Component, Input, OnChanges, OnInit } from '@angular/core';
import {
    CollaborationsRoles,
    ShoppingList,
    ShoppingListCollaboration
} from "../../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../../core-module/services/rest/shopping-list.service";
import { AuthService } from "../../../../core-module/services/auth.service";
import { ShoppingListCollaborationService } from "../../../../core-module/services/rest/shopping-list-collaboration.service";
import { Router } from "@angular/router";

const $ = window['jQuery'];

@Component({
    selector: 'share-collaborators-and-invites',
    templateUrl: './share-collaborators.component.html',
    styleUrls: ['./share-collaborators.component.css']
})
export class ShareCollaboratorsComponent {

    isSaving = false;

    @Input()
    list: ShoppingList;

    roles = CollaborationsRoles;

    constructor(
        private collaborationService: ShoppingListCollaborationService,
        private authService: AuthService,
        private router: Router
    ) {
    }

    onDeleteCollaboration(toDelete: ShoppingListCollaboration) {
        this.isSaving = true;

        this.collaborationService.deleteCollaboration(this.list, toDelete)
            .subscribe(_ => {
                this.isSaving = false;

                if (this.isMyCollaboration(toDelete)) {
                    this.redirectToHome();
                }
            });
    }

    onDeleteInvite(email: string) {
        // TODO: Implement
    }


    isMyCollaboration(collaboration: ShoppingListCollaboration) {
        return this.authService.user.id == collaboration.user.id;
    }

    private redirectToHome () {
        this.closeListShareSettingsModal();
        this.router.navigateByUrl('/');
    }

    private closeListShareSettingsModal () {
        $('#listSharingSettingsModal').modal('hide');
    }
}
