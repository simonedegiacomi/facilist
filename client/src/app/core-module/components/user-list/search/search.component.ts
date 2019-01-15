import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { ProductService } from "../../../services/rest/product.service";
import { Product } from "../../../models/product";
import { ShoppingList } from "../../../models/shopping-list";
import { AuthService } from "../../../services/auth.service";
import { SearchOnUserInput } from "../../../utils/SearchOnUserInput";

/**
 * Search bar to add products to the shopping list
 */
@Component({
    selector: 'user-list-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnDestroy {

    @Input() list: ShoppingList;

    /**
     * Show or hide the create product button
     */
    @Input() allowNewProducts: boolean;

    /**
     * Emits events when the create button product is pressed
     */
    @Output() addProduct = new EventEmitter<Product>();

    /**
     * Emits events when a new product is created
     */
    @Output() createProduct = new EventEmitter<string>();

    searchText = "";


    /**
     * Bidn input with logic to search
     */
    private search = new SearchOnUserInput(searchTerm => {
        this.isLoading = true;
        const includeUserProducts = this.authService.user != null;

        return this.productService.searchByNameAndShoppingListCategory(searchTerm, this.list.category, includeUserProducts)
    }, resultPage => this.onGotResults(resultPage.content));

    /**
     * Enable backdrop or not
     */
    isFocused = false;

    /**
     * Results products mapped by category name
     */
    productsByCategories: { [key:string]: Product[] };

    /**
     * Text currently present in the search field
     */
    filterText: string;

    isLoading = false;
    emptyResults = false;

    constructor(
        private productService: ProductService,
        private authService: AuthService
    ) { }

    ngOnDestroy() {
        this.search.unbind();
    }

    onUpdateSearchFilter(searchFilter: string) {
        this.isFocused = true;
        this.filterText = searchFilter;
        this.search.onInput(searchFilter);
    }

    onAddProduct (product: Product) {
        if (this.isProductAlreadyInList(product)) {
            return;
        }

        this.addProduct.emit(product);
        this.productsByCategories = {};
        this.isFocused = false;
        this.searchText = "";
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
        this.searchText = "";
    }


    /**
     * Cheks if a specified product it's already in the list
     * @param product
     */
    isProductAlreadyInList (product: Product): boolean {
        return this.list.products
            .map(relation => relation.product)
            .find(inList => inList.id == product.id) != null;
    }
}
