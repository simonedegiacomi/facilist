import { Component, OnInit } from '@angular/core';
import { ShoppingListCategoryService } from "../../../core-module/services/shopping-list-category.service";
import { Observable } from "rxjs";
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../core-module/services/shopping-list.service";


const $ = window['jQuery'];


@Component({
    selector: 'user-new-list',
    templateUrl: './new-list.component.html',
    styleUrls: ['./new-list.component.css']
})
export class NewListComponent implements OnInit {

    // TODO: Aggiungere pulsante per caricare l'icona

    categories$: Observable<ShoppingListCategory[]>;

    newList: ShoppingList = new ShoppingList();

    isSaving = false;

    constructor(
        private listCategoryService: ShoppingListCategoryService,
        private listService: ShoppingListService
    ) {
    }

    ngOnInit() {
        this.fetchListCategories();
    }

    fetchListCategories () {
        this.categories$ = this.listCategoryService.getAll();
    }

    get newListIcon () {
        if (this.newList.icon != null) {
            return this.newList.icon;
        } else if (this.newList.category != null) {
            return this.newList.category.icon;
        } else {
            return 'default-shopping-list-icon';
        }
    }

    onCreate () {
        this.isSaving = true;
        this.newList.icon = this.newListIcon;
        this.listService.create(this.newList).subscribe(l => {
            this.isSaving = false;
            $('#newListModal').modal('hide');
            // TODO: Redirect to the shopping list page
        });
    }

}
