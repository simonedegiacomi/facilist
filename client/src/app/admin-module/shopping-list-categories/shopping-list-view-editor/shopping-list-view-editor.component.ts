import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ShoppingListCategory } from "../../../core-module/models/shopping-list-category";
import { ProductCategory } from "../../../core-module/models/product-category";
import { switchMap } from "rxjs/operators";
import editorConfig from '../../../core-module/tinymceConfig';

import {
    SHOPPING_LIST_CATEGORY_NAME_CONFLICT,
    ShoppingListCategoryService
} from "../../../core-module/services/rest/shopping-list-category.service";
import { ProductCategoryService } from "../../../core-module/services/rest/product-category.service";
import { ForesquareCategory } from "../../../core-module/models/foresquare-category";
import { ShoppingList } from "../../../core-module/models/shopping-list";
import { Observable } from "rxjs";

const $ = window['jQuery'];

@Component({
    selector: 'admin-shopping-list-view-editor',
    templateUrl: './shopping-list-view-editor.component.html',
    styleUrls: ['./shopping-list-view-editor.component.css']
})
export class ShoppingListViewEditorComponent implements OnInit {


    isNew              = false;
    isEditing          = false;
    isSavingOrCreating = false;
    nameError          = false;

    private originalName: string;
    private originalDescription: string;

    category: ShoppingListCategory;

    /**
     * List of all products categories
     */
    productCategories: ProductCategory[];

    /**
     * Product categories included in the shopping list category
     */
    includedProductCategories: ProductCategory[];

    /**
     * List of all Foursquare categories
     */
    foresquareCategories: ForesquareCategory[];

    /**
     * Foursquare categories in the shopping list category
     */
    includedForesquareCategories: string[];

    /**
     * TinyMCE editor config
     */
    editorConfig = editorConfig;

    @Output() cancel  = new EventEmitter<ShoppingListCategory>();
    @Output() deleted = new EventEmitter<ShoppingListCategory>();
    @Output() created = new EventEmitter<ShoppingListCategory>();

    constructor(
        private categoryService: ShoppingListCategoryService,
        private productCategoryService: ProductCategoryService
    ) { }

    ngOnInit() {
        this.fetchProductCategories();
        this.fetchForesquareCategories();
    }

    private fetchForesquareCategories() {
        this.categoryService.getAllForesquareCategories()
            .subscribe(categories => this.foresquareCategories = categories);
    }

    fetchProductCategories() {
        this.productCategoryService.getAll()
            .subscribe(categories => this.productCategories = categories);
    }

    /**
     * Function called when the editor needs to display a different shopping list category
     * @param category
     */
    @Input() set shoppingListCategory(category: ShoppingListCategory) {
        const wasEditing = this.isEditing;
        if (this.isEditing) {
            this.onCancel();
        }

        this.category                     = category;
        this.includedProductCategories    = [];
        this.includedForesquareCategories = this.category.foursquareCategoryIds;
        this.nameError                    = false;

        if (category.id == null) {
            this.isNew = true;

            if (wasEditing) {
                // Let the description editor reset
                setTimeout(() => this.isEditing = true)
            } else {
                this.isEditing = true;
            }
        } else {
            this.isNew                     = false;
            this.originalName              = this.category.name;
            this.originalDescription       = this.category.description;
            this.includedProductCategories = this.category.productCategories;
        }
    }

    /**
     * Checks if a product category is already included in the list category
     * @param category
     */
    isIncluded(category: ProductCategory): boolean {
        return this.includedProductCategories
            .find(includedCategory => includedCategory.id == category.id) != null;
    }

    /**
     * Checks if a Foursquare category is already included in the list category
     * @param category
     */
    isForesquareCategoryIncluded(category: ForesquareCategory): boolean {
        return this.includedForesquareCategories.find(includedId => includedId == category.id) != null;
    }

    onToggleProductCategory(category: ProductCategory) {
        let index = this.includedProductCategories.findIndex(included => included.id == category.id);

        if (index < 0) {
            console.log('aggiungo');
            this.includedProductCategories.push(category);
        } else if (index >= 0) {
            console.log('rimuovo');
            this.includedProductCategories.splice(index, 1);
        }
    }

    onToggleForesquareCategory(category: ForesquareCategory) {
        let index = this.includedForesquareCategories.findIndex(includedId => includedId == category.id);

        if (index < 0) {
            this.includedForesquareCategories.push(category.id);
        } else {
            this.includedForesquareCategories.splice(index, 1);
        }
    }

    /**
     * This function send the request to create or save the shopping list category and then send a second request to
     * set the product categories of the list category
     */
    onSaveOrCreate() {
        this.isSavingOrCreating = true;
        this.nameError          = false;

        let observable: Observable<ShoppingListCategory>;
        if (this.isNew) {
            observable = this.categoryService.create(this.category);
        } else {
            observable = this.categoryService.update(this.category);
        }

        observable.pipe(
            switchMap(c => {
                if (this.isNew) {
                    this.category.id = c.id;
                }

                return this.categoryService.updateProductCategories(c, this.includedProductCategories)
            })
        ).subscribe(_ => {
            if (this.isNew) {
                this.isNew = false;
                this.created.emit(this.category);
            }

            this.isSavingOrCreating = false;
            this.isEditing          = false;
        }, err => {
            if (err == SHOPPING_LIST_CATEGORY_NAME_CONFLICT) {
                this.nameError = true;
            }
        })
    }

    onCancel() {
        this.isEditing            = false;
        this.category.name        = this.originalName;
        this.category.description = this.originalDescription;
        this.cancel.emit(this.category);
    }

    onDelete() {
        $(`#delete-category-${this.category.id}-warning`).modal('show');
    }

    onDeleteAfterWarning() {
        const toDelete = this.category;

        $(`#delete-category-${toDelete.id}-warning`).modal('hide');

        this.categoryService.delete(toDelete)
            .subscribe(_ => this.deleted.emit(toDelete));
    }
}
