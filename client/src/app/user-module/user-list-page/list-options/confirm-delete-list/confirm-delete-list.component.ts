import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from "../../../../core-module/services/auth.service";
import { Router } from "@angular/router";
import { ShoppingList } from "../../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../../core-module/services/rest/shopping-list.service";

const $ = window['jQuery'];

@Component({
    selector: 'confirm-delete-list',
    templateUrl: './confirm-delete-list.component.html',
    styleUrls: ['./confirm-delete-list.component.css']
})
export class ConfirmDeleteListComponent {

    @Input() list: ShoppingList;

    isDeleting = false;

    constructor(
        private listService: ShoppingListService,
        private router: Router
    ) {}


    onDelete () {
        this.isDeleting = true;
        this.listService.delete(this.list).subscribe(() => {
            this.isDeleting = false;

            this.closeModal();
            this.router.navigateByUrl('/');
        });
    }

    onCancel () {
        this.closeModal();
        this.openOptionsModal();
    }

    private closeModal () {
        $('#confirmDeleteListModal').modal('hide');
    }


    private openOptionsModal () {
        $('#listOptionsModal').modal('show');
    }
}
