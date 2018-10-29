import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Subject } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";
import { ProductService } from "../../../services/product.service";
import { Product } from "../../../models/product";
import { ShoppingList } from "../../../models/shopping-list";

@Component({
    selector: 'user-list-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

    @Input() list: ShoppingList;

    @Output() addProduct = new EventEmitter<Product>();

    @Output() createProduct = new EventEmitter();

    private filter = new Subject<string>();

    results: Product[];

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
            switchMap(filter => this.productService.searchByNameAndShoppingListCategory(filter, this.list.category))
        ).subscribe(resultPage => this.results = resultPage.content);
    }

    onUpdateSearchFilter(searchFilter: string) {
        this.filter.next(searchFilter);
    }

    onAddProduct (product: Product) {
        this.addProduct.emit(product);
        this.results = null;
    }
}
