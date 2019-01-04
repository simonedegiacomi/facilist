import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { NotLoggedInGuard } from "../core-module/services/guards/not-logged-in-guard.service";
import { AdminGuard } from "../core-module/services/guards/admin-guard.service";
import { ProductCategoriesComponent } from "./product-categories/product-categories.component";
import { ProductsComponent } from "./products/products.component";
import { ShoppingListCategoriesComponent } from "./shopping-list-categories/shopping-list-categories.component";
import { AdminRootComponent } from "./admin-root.component";


const routes: Routes = [
    {
        path: 'admin',
        component: AdminRootComponent,
        children: [
            {
                path: 'productCategories',
                component: ProductCategoriesComponent
            }, {
                path: 'products',
                component: ProductsComponent
            }, {
                path: 'shoppingListCategories',
                component: ShoppingListCategoriesComponent
            }, { // Redirect unknown admin path to products
                path: '**',
                redirectTo: 'products'
            }
        ],
        canActivate: [
            // The admin path can be reached only if the currently logged in user is an admin
            AdminGuard
        ]
    }
];


@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ],
    providers: [
        NotLoggedInGuard,
        AdminGuard
    ]
})
export class AdminRoutingModule {
}
