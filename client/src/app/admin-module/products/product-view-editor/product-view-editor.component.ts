import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from "../../../core-module/models/product";
import { ProductService } from "../../../core-module/services/product.service";
import { UploadService } from "../../../core-module/services/upload.service";
import { Observable, of } from "rxjs";
import { map, switchMap } from "rxjs/operators";
import { ProductCategory } from "../../../core-module/models/product-category";
import { ProductCategoryService } from "../../../core-module/services/product-category.service";

const $ = window['jQuery'];

@Component({
    selector: 'admin-product-view-editor',
    templateUrl: './product-view-editor.component.html',
    styleUrls: ['./product-view-editor.component.css']
})
export class ProductViewEditorComponent implements OnInit {

    isNew = false;
    isEditing = false;
    isSaving = false;

    @Input() product: Product;

    private originalName: string;

    iconUploaded: Observable<void> = of(null);

    categories: ProductCategory[];

    @Output() cancel: EventEmitter<void> = new EventEmitter();
    @Output() created: EventEmitter<Product> = new EventEmitter();
    @Output() deleted: EventEmitter<void> = new EventEmitter();

    constructor(
        private productService: ProductService,
        private categoryService: ProductCategoryService,
        private uploadService: UploadService
    ) { }

    ngOnInit() {
        if (this.product.id == null) {
            this.isNew      = true;
            this.isEditing  = true;

            this.fetchCategories();
        } else {
            this.originalName = this.product.name;
        }
    }

    private fetchCategories () {
        this.categoryService.getAll()
            .subscribe(categories => this.categories = categories);
    }

    onSelectIconFile (event: Event) {
        const files = event.target['files'];
        if (files.length < 0) {
            return;
        }

        const file = files[0];

       this.iconUploaded = this.uploadService.uploadImage(file).pipe(
            map(id => { this.product.icon = id })
        );
    }


    onSaveOrCreate () {
        this.isSaving = true;

        this.iconUploaded.pipe(
            switchMap(_ => {
                if (this.isNew) {
                    return this.productService.create(this.product);
                } else {
                    return this.productService.update(this.product);
                }
            })
        ).subscribe(_ => {
            if (this.isNew) {
                this.created.emit(this.product);
                this.isNew = false;
            }

            this.isSaving = false;
            this.isEditing = false;
        });
    }

    onCancel () {
        this.isEditing      = false;
        this.product.name   = this.originalName;
        this.cancel.emit();
    }

    onDelete () {
        $(`#delete-product-${this.product.id}-warning`).modal('show');
    }

    onDeleteProductAfterWarning () {
        $(`#delete-product-${this.product.id}-warning`).modal('hide');
        this.productService.delete(this.product)
            .subscribe(_ => this.deleted.emit());
    }
}
