import { Component, Input } from '@angular/core';
import {
    CollaborationsRoles,
    ShoppingList,
    ShoppingListCollaboration
} from "../../../core-module/models/shopping-list";
import { AuthService } from "../../../core-module/services/auth.service";
import { ShoppingListService } from "../../../core-module/services/rest/shopping-list.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

@Component({
    selector: 'list-options',
    templateUrl: './list-options.component.html',
    styleUrls: ['./list-options.component.css']
})
export class ListOptionsComponent  {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => this.closeModal()
    }];


    @Input() list: ShoppingList;

    isSaving = false;

    constructor(
        private listService: ShoppingListService,
        private authService: AuthService
    ) { }

    onDeleteList () {
        this.closeModal();
        this.openConfirmDeleteModal();
    }

    onCancel () {
        this.closeModal();
    }

    onSave () {
        this.isSaving = true;
        this.listService.update(this.list).subscribe(() => {
            this.isSaving = false;
            this.closeModal();
        })
    }

    closeModal () {
        $('#listOptionsModal').modal('hide');
    }

    openConfirmDeleteModal () {
        $('#confirmDeleteListModal').modal('show');
    }

    get userId(): number { return this.authService.user.id; }

    get isUserTheCreator(): boolean { return this.userId == this.list.creator.id }

    get userCollaboration(): ShoppingListCollaboration {
        return this.list.collaborations.find(c => c.user.id == this.userId);
    }

    get canUserUpdateListInfo(): boolean {
        return this.isUserTheCreator || this.userCollaboration.role === CollaborationsRoles.ADMIN;
    }
}
