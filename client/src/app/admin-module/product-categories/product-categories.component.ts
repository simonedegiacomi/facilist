import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ProductCategoryService } from "../../core-module/services/rest/product-category.service";
import { ProductCategory } from "../../core-module/models/product-category";
import { PagedResult } from "../../core-module/services/rest/MyRestService";
import { PaginationComponent } from "../../core-module/components/pagination/pagination.component";
import { SearchOnUserInput } from "../../core-module/utils/SearchOnUserInput";

const $ = window['jQuery'];

@Component({
    selector: 'app-product-categories',
    templateUrl: './product-categories.component.html',
    styleUrls: ['./product-categories.component.css']
})
export class ProductCategoriesComponent implements OnInit, OnDestroy {

    /**
     * Page of results currently displayed
     */
    page: PagedResult<ProductCategory>;

    /**
     * New category not created yet
     */
    newCategory: ProductCategory;

    /**
     * Bind search input to logic to search product categories
     */
    private search: SearchOnUserInput<PagedResult<ProductCategory>> = new SearchOnUserInput(
        searchTerm => this.categoriesService.searchByNameAndSortByName(searchTerm),
        page => this.page = page
    );

    /**
     * Object that shows the links to the other pages of results
     */
    @ViewChild('pagination') private pagination: PaginationComponent<ProductCategory>;

    /**
     * Product category that the user wants to tdelete
     */
    deletingCategory: ProductCategory;

    constructor(
        private categoriesService: ProductCategoryService
    ) { }

    ngOnInit() {
        this.fetchCategories();
    }

    ngOnDestroy() {
        this.search.unbind();
    }

    /**
     * Load the first page of categories
     */
    private fetchCategories () {
        this.categoriesService.getAllPaged()
            .subscribe(page => this.page = page);
    }

    onUpdateSearchFilter (searchFilter: string) {
        this.search.onInput(searchFilter);
    }

    createCategory () {
        this.newCategory = new ProductCategory();
    }

    onNewCategoryCreated (created: ProductCategory) {
        this.newCategory = null;
        this.pagination.reloadCurrentPage();
    }

    /**
     * Checks if we're loading categories
     */
    get loading (): boolean { return this.page == null; }

    /**
     * Return the list of categories of the current page
     */
    get categories (): ProductCategory[] { return this.page.content; }

    /**
     * Shows the delete warning dialog
     * @param category Category to delte
     */
    onDelete (category: ProductCategory) {
        this.deletingCategory = category;
        $('#deleteCategoryModal').modal('show');
    }

    /**
     * Actually delete the category, after the user has confirm on the dialog
     * @param category
     */
    onDeleteCategoryAfterWarning (category: ProductCategory) {
        $('#deleteCategoryModal').modal('hide');
        this.categoriesService.delete(category).subscribe(_ => {
            this.pagination.reloadCurrentPage();
            this.deletingCategory = null;
        });
    }
}
