import { Component, OnInit } from '@angular/core';
import { ShoppingListCategory } from "../../core-module/models/shopping-list-category";
import { ShoppingListCategoryService } from "../../core-module/services/shopping-list-category.service";

@Component({
    selector: 'app-shopping-list-categories',
    templateUrl: './shopping-list-categories.component.html',
    styleUrls: ['./shopping-list-categories.component.css']
})
export class ShoppingListCategoriesComponent implements OnInit {


    categories: ShoppingListCategory[];

    selected: ShoppingListCategory;


    constructor(
        private categoryService: ShoppingListCategoryService
    ) { }

    ngOnInit() {
        this.fetchCategories();
    }

    fetchCategories () {
        this.categoryService.getAll()
            .subscribe(categories => this.categories = categories)
    }

    onCreate () {
        const newCategory = new ShoppingListCategory();
        newCategory.name = "Nuova categoria";

        this.categories.splice(0, 0, newCategory);
        this.selected = newCategory;
    }

    onCancel (cancelled: ShoppingListCategory) {
        if (this.selected == cancelled && this.selected.id == null) { // is new
            this.selected = null;
            this.categories.splice(0, 1);
        }
    }

    onDeleted (deleted: ShoppingListCategory) {
        this.selected = null;

        const index = this.categories.indexOf(deleted);
        this.categories.splice(index, 1);
    }
}
