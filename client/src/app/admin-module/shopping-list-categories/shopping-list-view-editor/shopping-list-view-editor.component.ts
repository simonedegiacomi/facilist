import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { Observable, of } from "rxjs";
import { ProductCategory } from "../../../core-module/models/product-category";
import { map, switchMap } from "rxjs/operators";
import { UploadService } from "../../../core-module/services/upload.service";
import {
    SHOPPING_LIST_CATEGORY_NAME_CONFLICT,
    ShoppingListCategoryService
} from "../../../core-module/services/shopping-list-category.service";
import { ProductCategoryService } from "../../../core-module/services/product-category.service";

const $ = window['jQuery'];

@Component({
    selector: 'admin-shopping-list-view-editor',
    templateUrl: './shopping-list-view-editor.component.html',
    styleUrls: ['./shopping-list-view-editor.component.css']
})
export class ShoppingListViewEditorComponent implements OnInit {

    isNew = false;
    isEditing = false;
    isSavingOrCreating = false;
    nameError = false;

    private originalName: string;
    private originalDescription: string;

    category: ShoppingListCategory;

    productCategories: ProductCategory[];
    includedProductCategories: ProductCategory[];

    iconUploaded: Observable<void> = of(null);

    editorConfig = {
        menubar: false,
        toolbar: 'undo, redo | cut, copy, paste, selectall | bold, italic | subscript, superscript | formatselect',
        block_formats: 'Paragrafo=p;Titolo 2=h3;Sottotitolo 3=h4;Titolino 4=h5;',
        branding: false,

        language: 'it',
        language_url: '/tinymce/langs/it.js',

        skin_url: '/tinymce/skins/lightgray'
    };

    @Output() cancel: EventEmitter<ShoppingListCategory> = new EventEmitter<ShoppingListCategory>();
    @Output() deleted: EventEmitter<ShoppingListCategory> = new EventEmitter<ShoppingListCategory>();

    constructor(
        private categoryService: ShoppingListCategoryService,
        private productCategoryService: ProductCategoryService,
        private uploadService: UploadService
    ) { }

    ngOnInit() {
        this.fetchProductCategories();
    }

    fetchProductCategories () {
        this.productCategoryService.getAllSortedByName()
            .subscribe(categories => this.productCategories = categories);
    }

    @Input() set shoppingListCategory (category: ShoppingListCategory) {
        const wasEditing = this.isEditing;
        if (this.isEditing) {
            this.onCancel();
        }

        this.category                   = category;
        this.includedProductCategories  = [];
        this.nameError                  = false;

        if (category.id == null) {
            this.isNew      = true;

            if (wasEditing) {
                // Let the description editor reset
                setTimeout(() => this.isEditing = true)
            } else {
                this.isEditing  = true;
            }
        } else {
            this.isNew                  = false;
            this.originalName           = this.category.name;
            this.originalDescription    = this.category.description;
            this.categoryService.getProductCategoriesOfShoppingListCategory(category)
                .subscribe(included => this.includedProductCategories = included);
        }
    }


    isIncluded (category: ProductCategory): boolean {
        for (let included of this.includedProductCategories) {
            if (included.id == category.id) {
                return true;
            }
        }

        return false;
    }

    onToggleProductCategory (category: ProductCategory, checked: boolean) {
        let index = this.includedProductCategories.findIndex(included => included.id == category.id);

        if (checked && index < 0) {
            this.includedProductCategories.push(category);
        } else if (!checked && index >= 0) {
            this.includedProductCategories.splice(index, 1);
        }
    }

    onSelectIconFile () {
        const files = event.target['files'];
        if (files.length < 0) {
            return;
        }

        const file = files[0];

        this.iconUploaded = this.uploadService.uploadImage(file).pipe(
            map(id => { this.category.icon = id })
        );
    }

    onSaveOrCreate () {
        this.isSavingOrCreating = true;
        this.nameError = false;

        // TODO: Async lock the selection

        this.iconUploaded.pipe(
            switchMap(_ => {
                if (this.isNew) {
                    return this.categoryService.create(this.category);
                } else {
                    return this.categoryService.update(this.category);
                }
            }),
            switchMap(_ => this.categoryService.updateProductCategories(this.category, this.includedProductCategories))
        ).subscribe(_ => {
            if (this.isNew) {
                this.isNew = false;
            }

            this.isSavingOrCreating = false;
            this.isEditing = false;
        }, err => {
            if (err == SHOPPING_LIST_CATEGORY_NAME_CONFLICT) {
                this.nameError = true;
            }
        })
    }

    onCancel () {
        this.isEditing              = false;
        this.category.name          = this.originalName;
        this.category.description   = this.originalDescription;
        this.cancel.emit(this.category);
    }

    onDelete () {
        $(`#delete-category-${this.category.id}-warning`).modal('show');
    }

    onDeleteAfterWarning () {
        const toDelete = this.category;

        $(`#delete-category-${toDelete.id}-warning`).modal('hide');

        this.categoryService.delete(toDelete)
            .subscribe(_ => this.deleted.emit(toDelete));
    }
}
