import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from "./admin-routing.module";
import { ProductCategoriesComponent } from './product-categories/product-categories.component';
import { ProductsComponent } from './products/products.component';
import { EditorModule } from "@tinymce/tinymce-angular";
import { CategoryViewEditorComponent } from './product-categories/category-view-editor/category-view-editor.component';
import { FormsModule } from "@angular/forms";
import { ProductViewEditorComponent } from './products/product-view-editor/product-view-editor.component';
import { PaginationComponent } from "../core-module/components/pagination/pagination.component";
import { ShoppingListCategoriesComponent } from './shopping-list-categories/shopping-list-categories.component';
import { ShoppingListViewEditorComponent } from './shopping-list-categories/shopping-list-view-editor/shopping-list-view-editor.component';
import { AdminRootComponent } from './admin-root.component';
import { CoreModule } from "../core-module/core.module";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        EditorModule,
        AdminRoutingModule,
        CoreModule
    ],
    declarations: [
        PaginationComponent,

        ProductCategoriesComponent,
        ProductsComponent,
        CategoryViewEditorComponent,
        ProductViewEditorComponent,
        ShoppingListCategoriesComponent,
        ShoppingListViewEditorComponent,
        AdminRootComponent
    ]
})
export class AdminModule {
}
