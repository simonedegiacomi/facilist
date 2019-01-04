import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {
    ProductCategoryService,
    ProductCategoryWithProductsCount
} from "../../core-module/services/rest/product-category.service";
import { ProductCategory } from "../../core-module/models/product-category";
import { Product } from "../../core-module/models/product";
import { ProductService } from "../../core-module/services/rest/product.service";
import { PagedResult } from "../../core-module/services/rest/MyRestService";
import { PaginationComponent } from "../../core-module/components/pagination/pagination.component";
import { SearchOnUserInput } from "../../core-module/utils/SearchOnUserInput";

@Component({
    selector: 'app-products',
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {

    /**
     * List of all the categories
     */
    categories: ProductCategoryWithProductsCount[];

    /**
     * Selected category, of which products of the current page are shown. When this is null, page of all the products
     * is shown
     */
    selectedCategory: ProductCategory;

    /**
     * Currently displayed page
     */
    productsPage: PagedResult<Product>;

    /**
     * Input field of the search (we need the reference to clear the input when the user select another product cateogry
     */
    @ViewChild('searchFilter') searchFilter: ElementRef;

    /**
     * Bind search input to logic to search products
     */
    private search: SearchOnUserInput<PagedResult<Product>> = new SearchOnUserInput(
        searchTerm => {
            if (this.selectedCategory) {
                return this.productsService.searchByCategoryAndNameAndSortByName(this.selectedCategory, searchTerm);
            } else {
                return this.productsService.searchByNameAndSortByName(searchTerm);
            }
        },
        page => this.productsPage = page
    );

    /**
     * Reference to a new product not saved yet
     */
    newProduct: Product;

    /**
     * Shows the page links
     */
    @ViewChild('pagination') private pagination: PaginationComponent<ProductCategory>;

    constructor(
        private categoryService: ProductCategoryService,
        private productsService: ProductService
    ) { }

    ngOnInit() {
        this.fetchCategories();
        this.fetchAllProducts();
    }

    ngOnDestroy() {
        this.search.unbind();
    }

    private fetchCategories () {
        this.categoryService.getAllWithProductsCount()
            .subscribe(categories => this.categories = categories);
    }

    private fetchAllProducts () {
        this.productsService.getAllPaged()
            .subscribe(page =>this.productsPage = page)
    }

    /**
     * Load product of all the categories
     */
    onShowAllProducts() {
        this.selectedCategory = null;
        this.fetchAllProducts();
    }

    /**
     * Load products of a specified category
     * @param category
     */
    onSelectCategory (category: ProductCategory) {
        this.searchFilter.nativeElement.value = "";
        this.selectedCategory = category;
        this.productsService.getAllOfCategoryPagedSortedByName(category)
            .subscribe(page => this.productsPage = page);

    }

    /**
     * Checks if it's loading products
     */
    get loadingProducts (): boolean { return this.productsPage == null; }

    /**
     * Return the list of product in the current page
     */
    get products (): Product[] { return this.productsPage.content; }


    onUpdateSearchFilter (searchFilter: string) {
        this.search.onInput(searchFilter);
    }

    onCreateProduct () {
        this.newProduct = new Product();

        if (this.selectedCategory) {
            this.newProduct.categoryId = this.selectedCategory.id;
        }
    }

    onProductCreated () {
        this.newProduct = null;
        this.pagination.reloadCurrentPage();
    }

    onProductDeleted () {
        this.pagination.reloadCurrentPage();
    }
}
