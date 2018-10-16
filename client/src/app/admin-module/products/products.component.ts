import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProductCategoryService } from "../../core-module/services/product-category.service";
import { ProductCategory } from "../../core-module/models/product-category";
import { Product } from "../../core-module/models/product";
import { ProductService } from "../../core-module/services/product.service";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";
import { PagedResult } from "../../core-module/services/MyRestService";
import { Subject } from "rxjs";
import { PaginationComponent } from "../../core-module/components/pagination/pagination.component";

@Component({
    selector: 'app-products',
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

    categories: ProductCategory[];
    selectedCategory: ProductCategory;

    productsPage: PagedResult<Product>;

    @ViewChild('searchFilter') searchFilter: ElementRef;

    private filter = new Subject<string>();

    newProduct: Product;

    @ViewChild('pagination') private pagination: PaginationComponent<ProductCategory>;

    constructor(
        private categoryService: ProductCategoryService,
        private productsService: ProductService
    ) {
    }

    ngOnInit() {
        this.fetchCategories();
        this.fetchAllProducts();
        this.setupSearch();
    }

    private fetchCategories () {
        this.categoryService.getAll().subscribe(categories => this.categories = categories);
    }

    private fetchAllProducts () {
        this.productsService.getAllPaged().subscribe(page =>this.productsPage = page)
    }

    private setupSearch () {
        this.filter.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(filter => {
                if (this.selectedCategory) {
                    return this.productsService.searchByCategoryAndNameAndSortByName(this.selectedCategory, filter);
                } else {
                    return this.productsService.searchByNameAndSortByName(filter);
                }
            })
        ).subscribe(page => this.productsPage = page);
    }

    onShowAllProducts() {
        this.selectedCategory = null;
        this.fetchAllProducts();
    }

    onSelectCategory (category: ProductCategory) {
        this.searchFilter.nativeElement.value = "";
        this.selectedCategory = category;
        this.productsService.getAllOfCategoryPagedSortedByName(category)
            .subscribe(page => this.productsPage = page);

    }

    get loadingProducts (): boolean { return this.productsPage == null; }

    get products (): Product[] { return this.productsPage.content; }


    onUpdateSearchFilter (searchFilter: string) {
        this.filter.next(searchFilter);
    }

    onCreateProduct () {
        this.newProduct = new Product();

        if (this.selectedCategory) {
            this.newProduct.category = this.selectedCategory;
        }
    }

    onProductCreated (product: Product) {
        this.newProduct = null;
        this.pagination.reloadCurrentPage();
    }

    onProductDeleted () {
        this.pagination.reloadCurrentPage();
    }
}
