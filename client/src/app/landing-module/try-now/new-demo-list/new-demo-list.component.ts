import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ShoppingListCategoryService } from "../../../core-module/services/shopping-list-category.service";
import { Observable } from "rxjs";
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { ShoppingListService } from "../../../core-module/services/shopping-list.service";

@Component({
    selector: 'app-new-demo-list',
    templateUrl: './new-demo-list.component.html',
    styleUrls: ['./new-demo-list.component.css']
})
export class NewDemoListComponent implements OnInit {

    @Output() listCreated = new EventEmitter<ShoppingList>();

    categories$: Observable<ShoppingListCategory[]>;

    newList = new ShoppingList();

    constructor(
        private shoppingListCategoryService: ShoppingListCategoryService,
        private shoppingListService: ShoppingListService
    ) {
        this.newList.name        = "Lista demo";
        this.newList.description = "Una lista di prova";
    }

    ngOnInit() {
        this.categories$ = this.shoppingListCategoryService.getAll();
    }

    onCreate() {
        this.newList.icon = this.newList.category.icon;
        this.shoppingListService.create(this.newList)
            .subscribe(list => this.listCreated.emit(list));
    }
}
