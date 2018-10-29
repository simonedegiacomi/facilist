import { Component, OnInit } from '@angular/core';
import { ShoppingListService } from "../../core-module/services/shopping-list.service";
import { LocalStorageShoppingListService } from "../services/local-storage-shopping-list.service";
import { Router } from "@angular/router";
import { ShoppingList } from "../../core-module/models/shopping-list";

@Component({
    templateUrl: './try-now.component.html',
    styleUrls: ['./try-now.component.css'],
    providers: [{
        provide: ShoppingListService,
        useClass: LocalStorageShoppingListService
    }]
})
export class TryNowComponent implements OnInit {

    list: ShoppingList;

    constructor(
        private router: Router,
        private shoppingListService: ShoppingListService
    ) {
    }

    ngOnInit() {
        this.loadList();
    }


    loadList () {
        this.shoppingListService.getMyShoppingLists().subscribe(lists => {
            if (lists.length > 0) {
                this.shoppingListService.getById(0).subscribe(list => this.list = list);
            }
        });
    }

    onListCreated (list: ShoppingList) {
        this.list = list;
    }

    onBack(){
        this.router.navigateByUrl('/');
    }

    onOpenShareSettings () {
        alert('Iscriviti!!1!');
    }

    onOpenSettings () {
        alert('Iscriviti!!1!');
    }
}
