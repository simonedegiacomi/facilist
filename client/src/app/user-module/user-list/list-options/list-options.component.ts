import { Component, Input, OnInit } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../core-module/services/shopping-list.service";
import { AuthService } from "../../../core-module/services/auth.service";

@Component({
    selector: 'list-options',
    templateUrl: './list-options.component.html',
    styleUrls: ['./list-options.component.css']
})
export class ListOptionsComponent implements OnInit {

    @Input() list: ShoppingList;

    constructor(
        private listService: ShoppingListService,
        private authService: AuthService
    ) {
    }

    ngOnInit() {
    }

    deleteList () {
        // TODO: Ask for confirmation
        this.listService.delete(this.list).subscribe(() => {
            console.log('deleted');
            // TODO: Redirect
        });
    }

    get isUserTheCreator () { return this.authService.user.id == this.list.creator.id }
}
