import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProductCategory } from "../../../core-module/models/product-category";
import { CATEGORY_NAME_CONFLICT, ProductCategoryService } from "../../../core-module/services/product-category.service";
import { Observable, of } from "rxjs";
import { UploadService } from "../../../core-module/services/upload.service";
import { catchError, map } from "rxjs/operators";
import editorConfig from '../../../core-module/tinymceConfig';

@Component({
    selector: 'admin-category-view-editor',
    templateUrl: './category-view-editor.component.html',
    styleUrls: ['./category-view-editor.component.css']
})
export class CategoryViewEditorComponent implements OnInit {

    @Input() category: ProductCategory;

    private originalName: string;
    private originalDescription: string;

    @Output() cancel: EventEmitter<void> = new EventEmitter();
    @Output() created: EventEmitter<ProductCategory> = new EventEmitter();
    @Output() delete: EventEmitter<ProductCategory> = new EventEmitter();

    isNew               = false;
    isEditing           = false;
    isSavingOrCreating  = false;

    nameConflict        = false;

    iconUploaded: Observable<void> = of(null);

    editorConfig = editorConfig;

    constructor(
        private categories: ProductCategoryService,
        private uploadService: UploadService
    ) { }

    ngOnInit() {
        if (this.category.id <= 0) {
            this.isNew      = true;
            this.isEditing  = true;
        } else {
            this.originalName = this.category.name;
            this.originalDescription = this.category.description;
        }
    }

    onSaveOrCreate () {
        this.isSavingOrCreating = true;
        this.nameConflict       = false;

       this.iconUploaded.subscribe(_ => this.saveOrCreate());
    }

    saveOrCreate () {
        if (this.isNew) {
            this.categories.create(this.category).subscribe(
                category => this.onSavedOrCreated(category),
                err => this.onErrorWhileSavingOrCreating(err)
            );
        } else {
            this.categories.update(this.category).subscribe(
                category => this.onSavedOrCreated(category),
                err => this.onErrorWhileSavingOrCreating(err)
            );
        }
    }

    onSavedOrCreated (category: ProductCategory | any) {
        if (this.isNew) {
            this.isNew = false;
            this.created.emit(category);
        }

        this.isEditing          = false;
        this.isSavingOrCreating = false;
    }

    onErrorWhileSavingOrCreating (error: any) {
        this.isSavingOrCreating = false;

        if (error === CATEGORY_NAME_CONFLICT) {
            this.nameConflict = true;
        }
    }

    onSelectIconFile (evt: Event) {
        const files = evt.target['files'];
        if (files.length < 0) {
            return;
        }

        const file = files[0];

        this.startIconUpload(file);
    }

    startIconUpload (file: File) {
        this.iconUploaded = this.uploadService.uploadImage(file).pipe(
            map(id => { this.category.icon = id }),
            catchError(_ => <Observable<void>> of(null))
        );
    }

    // TODO: Use Angular Form so validation is automatic
    get isValid (): boolean { return this.category.name != null && this.category.name != ""
        && this.category.description != null && this.category.description != ""; }

    onCancel () {
        this.isEditing              = false;
        this.category.name          = this.originalName;
        this.category.description   = this.originalDescription;
        this.cancel.emit();
    }

    onDelete () {
        this.delete.emit(this.category);
    }
}
