import { Component, OnInit } from '@angular/core';
import { ShoppingListCategoryService } from "../../core-module/services/shopping-list-category.service";
import { Observable } from "rxjs";
import { ShoppingListCategory } from "../../core-module/models/shopping-list-category";
import { ShoppingListPreview, ShoppingListService } from "../../core-module/services/shopping-list.service";
import { ShoppingList } from "../../core-module/models/shopping-list";

@Component({
    templateUrl: './user-lists.component.html',
    styleUrls: ['./user-lists.component.css']
})
export class UserListsComponent implements OnInit {

    lists$: Observable<ShoppingListPreview[]>;

    newList = new ShoppingList();

    newListCategory: ShoppingListCategory;

    isCreating = false;

    constructor(
        private listCategoryService: ShoppingListCategoryService,
        private listService: ShoppingListService
    ) {
        this.lists$ = this.listService.getMyShoppingLists()
    }

    ngOnInit() {
    }


    onCreateNewList() {
        this.isCreating = true;
        this.newList.category = this.newListCategory;
        this.newList.icon = this.newListCategory.icon;

        this.listService.create(this.newList).subscribe(() => {
            this.newList    = new ShoppingList();
            this.isCreating = false;
        });
    }

    onCreateNewList () {

    }
}
