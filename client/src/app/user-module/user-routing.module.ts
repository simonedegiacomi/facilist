import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { UserGuard } from "../core-module/services/guards/user-guard.service";
import { UserRootComponent } from "./user-root.component";
import { UserListsComponent } from "./user-lists/user-lists.component";
import { UserListPageComponent } from "./user-list-page/user-list-page.component";

const routes: Routes = [
    {
        path: 'user',
        component: UserRootComponent,
        children: [
            {
                path: '',
                component: UserListsComponent
            }, {
                path: 'shoppingLists/:id',
                component: UserListPageComponent
            }
        ],

        canActivate: [
            UserGuard
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
        UserGuard
    ]
})
export class UserRoutingModule {
}
