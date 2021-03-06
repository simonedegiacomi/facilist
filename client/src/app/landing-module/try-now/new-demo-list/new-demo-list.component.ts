import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ShoppingListCategoryService } from "../../../core-module/services/rest/shopping-list-category.service";
import { Observable } from "rxjs";
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { ShoppingListService } from "../../../core-module/services/rest/shopping-list.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";
import { Router } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";

/**
 * Component to create a new demo shopping list. The user will be able to select only the category
 */
@Component({
    selector: 'app-new-demo-list',
    templateUrl: './new-demo-list.component.html',
    styleUrls: ['./new-demo-list.component.css']
})
export class NewDemoListComponent implements OnInit {

    @Output() listCreated = new EventEmitter<ShoppingList>();

    categories$: Observable<ShoppingListCategory[]>;

    newList = new ShoppingList();

    buttons: NotebookSheetButton[] = [{
        title: 'Chiudi',
        iconClass: 'close-icon',
        onClick: () => this.router.navigateByUrl('/')
    }];

    constructor(
        private shoppingListCategoryService: ShoppingListCategoryService,
        private shoppingListService: ShoppingListService,
        private router: Router,
        private translate: TranslateService
    ) {
        // Localize name and description of demo list
        translate.get('landing.tryNow.newDemoList.list.name')
            .subscribe(name => this.newList.name = name);

        translate.get('landing.tryNow.newDemoList.list.description')
            .subscribe(description => this.newList.description = description);
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
