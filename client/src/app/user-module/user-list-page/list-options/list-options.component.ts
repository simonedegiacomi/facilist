import { Component, Input } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { AuthService } from "../../../core-module/services/auth.service";
import { Router } from "@angular/router";
import { ShoppingListService } from "../../../core-module/services/rest/shopping-list.service";

const $ = window['jQuery'];

@Component({
    selector: 'list-options',
    templateUrl: './list-options.component.html',
    styleUrls: ['./list-options.component.css']
})
export class ListOptionsComponent  {

    @Input() list: ShoppingList;

    isSaving = false;

    constructor(
        private listService: ShoppingListService,
        private authService: AuthService,
        private router: Router
    ) {
    }

    onDeleteList () {
        // TODO: Ask for confirmation
        this.isSaving = true;
        this.listService.delete(this.list).subscribe(() => {
            this.isSaving = false;
            this.closeModal();
            this.router.navigateByUrl('/');
        });
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
        $('#closeListOptions').click();
    }

    get isUserTheCreator () { return this.authService.user.id == this.list.creator.id }
}
