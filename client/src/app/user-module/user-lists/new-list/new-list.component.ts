import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ShoppingListCategoryService } from "../../../core-module/services/rest/shopping-list-category.service";
import { Observable } from "rxjs";
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { ShoppingListService } from "../../../core-module/services/rest/shopping-list.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];


@Component({
    selector: 'user-new-list',
    templateUrl: './new-list.component.html',
    styleUrls: ['./new-list.component.css']
})
export class NewListComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => this.closeModal()
    }];


    categories$: Observable<ShoppingListCategory[]>;

    newList: ShoppingList = new ShoppingList();

    isSaving = false;

    @Output()
    newListCreated = new EventEmitter<ShoppingList>();

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

    set newListIcon (image: string) {
        this.newList.icon = image;
    }



    onCreate () {
        this.isSaving = true;
        this.newList.icon = this.newListIcon;
        this.listService.create(this.newList).subscribe(l => {
            this.isSaving = false;
            this.closeModal();

            this.newListCreated.emit(this.newList);

            this.newList = new ShoppingList();
        });
    }

    private closeModal () {
        $('#newListModal').modal('hide')
    }

}
