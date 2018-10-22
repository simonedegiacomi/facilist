import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { UserGuard } from "../core-module/services/guards/user-guard.service";
import { UserRootComponent } from "./user-root.component";
import { UserDashboardComponent } from "./user-dashboard/user-dashboard.component";
import { UserListsComponent } from "./user-lists/user-lists.component";
import { UserListComponent } from "./user-list/user-list.component";

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
                component: UserListComponent
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
