import { Component, OnInit, ViewChild } from '@angular/core';
import { ProductCategoryService } from "../../core-module/services/product-category.service";
import { ProductCategory } from "../../core-module/models/product-category";
import { Subject } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";
import { PagedResult } from "../../core-module/services/MyRestService";
import { PaginationComponent } from "../../core-module/components/pagination/pagination.component";

const $ = window['jQuery'];

@Component({
    selector: 'app-product-categories',
    templateUrl: './product-categories.component.html',
    styleUrls: ['./product-categories.component.css']
})
export class ProductCategoriesComponent implements OnInit {

    page: PagedResult<ProductCategory>;
    newCategory: ProductCategory;

    private filter = new Subject<string>();


    @ViewChild('pagination') private pagination: PaginationComponent<ProductCategory>;

    deletingCategory: ProductCategory;

    constructor(
        private categoriesService: ProductCategoryService
    ) {
    }

    ngOnInit() {
        this.fetchCategories();
        this.setUpSearch();
    }


    fetchCategories () {
        this.categoriesService.getAllPaged()
            .subscribe(page => this.page = page);
    }

    setUpSearch () {
        this.filter.pipe(
            debounceTime(300),

            distinctUntilChanged(),

            //switchMap(filter => this.categoriesService.searchByNameAndSortByName(filter))
        ).subscribe(page => this.page = page);
    }

    onUpdateSearchFilter (searchFilter: string) {
        this.filter.next(searchFilter);
    }

    createCategory () {
        this.newCategory = new ProductCategory();
    }

    onNewCategoryCreated (created: ProductCategory) {
        this.newCategory = null;
        this.pagination.reloadCurrentPage();
    }


    get loading (): boolean { return this.page == null; }

    get categories (): ProductCategory[] { return this.page.content; }


    onDelete (category: ProductCategory) {
        this.deletingCategory = category;
        $('#deleteCategoryModal').modal('show');
    }

    onDeleteCategoryAfterWarning (category: ProductCategory) {
        $('#deleteCategoryModal').modal('hide');
        this.categoriesService.delete(category).subscribe(_ => {
            this.pagination.reloadCurrentPage();
            this.deletingCategory = null;
        });
    }
}
