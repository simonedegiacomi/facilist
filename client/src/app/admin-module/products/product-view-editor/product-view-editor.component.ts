import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from "../../../core-module/models/product";
import { ProductService } from "../../../core-module/services/rest/product.service";
import { map, switchMap } from "rxjs/operators";
import { ProductCategory } from "../../../core-module/models/product-category";
import { ProductCategoryService } from "../../../core-module/services/rest/product-category.service";

const $ = window['jQuery'];

@Component({
    selector: 'admin-product-view-editor',
    templateUrl: './product-view-editor.component.html',
    styleUrls: ['./product-view-editor.component.css']
})
export class ProductViewEditorComponent implements OnInit {

    isNew     = false;
    isEditing = false;
    isSaving  = false;

    @Input() product: Product;

    private originalName: string;

    categories: ProductCategory[];

    @Output() cancel: EventEmitter<void>     = new EventEmitter();
    @Output() created: EventEmitter<Product> = new EventEmitter();
    @Output() deleted: EventEmitter<void>    = new EventEmitter();

    constructor(
        private productService: ProductService,
        private categoryService: ProductCategoryService
    ) {
    }

    ngOnInit() {
        if (this.product.id == null) {
            this.isNew     = true;
            this.isEditing = true;

            this.fetchCategories();
        } else {
            this.originalName = this.product.name;
        }
    }

    private fetchCategories() {
        this.categoryService.getAll()
            .subscribe(categories => this.categories = categories);
    }

    onSelectCategory () {
        if (this.product.icon == null) {
            this.product.icon = this.product.category.icon;
        }
    }

    onSaveOrCreate() {
        this.isSaving = true;

        if (this.isNew) {
            this.product.categoryId = this.product.category.id;
            return this.productService.create(this.product).subscribe(_ => {
                this.created.emit(this.product);
                this.isNew = false;
            });
        } else {
            return this.productService.update(this.product).subscribe(_ => {
                this.isSaving  = false;
                this.isEditing = false;
            });
        }
    }

    onCancel() {
        this.isEditing    = false;
        this.product.name = this.originalName;
        this.cancel.emit();
    }

    onDelete() {
        $(`#delete-product-${this.product.id}-warning`).modal('show');
    }

    onDeleteProductAfterWarning() {
        $(`#delete-product-${this.product.id}-warning`).modal('hide');
        this.productService.delete(this.product)
            .subscribe(_ => this.deleted.emit());
    }
}
