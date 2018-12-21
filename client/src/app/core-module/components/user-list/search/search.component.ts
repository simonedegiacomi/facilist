import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Subject } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";
import { ProductService } from "../../../services/rest/product.service";
import { Product } from "../../../models/product";
import { ShoppingList } from "../../../models/shopping-list";

@Component({
    selector: 'user-list-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

    @Input() list: ShoppingList;
    @Input() allowNewProducts: boolean;

    @Output() addProduct = new EventEmitter<Product>();

    @Output() createProduct = new EventEmitter<string>();

    private filter = new Subject<string>();

    isFocused = false;

    productsByCategories: { [key:string]: Product[] };

    filterText: string;

    isLoading = false;
    emptyResults = false;

    constructor(
        private productService: ProductService
    ) {
    }

    ngOnInit() {
        this.setupSearch();
    }

    setupSearch() {
        this.filter.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(filter => {
                this.isLoading = true;
                return this.productService.searchByNameAndShoppingListCategory(filter, this.list.category)
            })
        ).subscribe(resultPage => this.onGotResults(resultPage.content));
    }

    onUpdateSearchFilter(searchFilter: string) {
        this.isFocused = true;
        this.filterText = searchFilter;
        this.filter.next(searchFilter);
    }

    onAddProduct (product: Product) {
        if (this.isProductAlreadyInList(product)) {
            return;
        }

        this.addProduct.emit(product);
        this.productsByCategories = {};
        this.isFocused = false;
    }

    onGotResults (results: Product[]) {
        this.isLoading = false;
        this.productsByCategories = {};
        this.emptyResults = results.length <= 0;

        for (let result of results) {

            if (this.productsByCategories[result.category.name] == null) {
                this.productsByCategories[result.category.name] = [];
            }

            this.productsByCategories[result.category.name].push(result);
        }
    }

    createCustomProduct() {
        this.createProduct.emit(this.filterText);
        this.isFocused = false;
    }


    isProductAlreadyInList (product: Product): boolean {
        return this.list.products
            .map(relation => relation.product)
            .find(inList => inList.id == product.id) != null;
    }
}
