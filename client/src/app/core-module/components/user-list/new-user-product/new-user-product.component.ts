import { Component, Input, OnInit } from '@angular/core';
import { defaultProductIcon, Product } from "../../../models/product";
import { ProductCategoryService } from "../../../services/rest/product-category.service";
import { ProductCategory } from "../../../models/product-category";
import { Observable } from "rxjs";
import { ProductService } from "../../../services/rest/product.service";
import { ShoppingList } from "../../../models/shopping-list";
import { ShoppingListService } from "../../../services/rest/shopping-list.service";
import { switchMap } from "rxjs/operators";
import { NotebookSheetButton } from "../../notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

@Component({
    selector: 'app-new-user-product',
    templateUrl: './new-user-product.component.html',
    styleUrls: ['./new-user-product.component.css']
})
export class NewUserProductComponent implements OnInit {


    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => this.closeModal()
    }];


    @Input() customProductName: string;

    @Input() shoppingList: ShoppingList;

    categories$: Observable<ProductCategory[]>;

    isSaving = false;

    newProduct = new Product();

    constructor(
        private productCategoryService: ProductCategoryService,
        private productService: ProductService,
        private shoppingListService: ShoppingListService
    ) { }

    ngOnInit() {
        this.categories$ = this.productCategoryService.getAll();
    }


    get newProductIcon(): string {
        if (this.newProduct.icon != defaultProductIcon) {
            return this.newProduct.icon;
        } else if (this.newProduct.category != null) {
            return this.newProduct.category.icon;
        } else {
            return defaultProductIcon;
        }
    }

    set newProductIcon(icon: string) {
        this.newProduct.icon = icon;
    }

    onSave() {
        this.isSaving = true;
        
        this.newProduct.name       = this.customProductName;
        this.newProduct.icon       = this.newProductIcon;
        this.newProduct.categoryId = this.newProduct.category.id;

        this.productService.create(this.newProduct).pipe(
            switchMap(product => this.shoppingListService.addProduct(this.shoppingList, product))
        ).subscribe(() => {
            this.newProduct = new Product();
            this.isSaving = false;
            this.closeModal();
        });
    }

    private closeModal () {
        $('#newUserProductModal').modal('hide');
    }
}
