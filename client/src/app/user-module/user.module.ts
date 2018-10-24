import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from "./user-routing.module";
import { UserRootComponent } from './user-root.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserListsComponent } from './user-lists/user-lists.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CoreModule } from "../core-module/core.module";
import { NewListComponent } from './user-lists/new-list/new-list.component';
import { UserListComponent } from './user-list/user-list.component';
import { SearchComponent } from './user-list/search/search.component';
import { UserProductListNoteComponent } from './user-list/user-product-list-note/user-product-list-note.component';
import { ListShareSettingsComponent } from './user-list/list-share-settings/list-share-settings.component';
import { ListOptionsComponent } from './user-list/list-options/list-options.component';

@NgModule({
    imports: [
        FormsModule,
        CommonModule,
        ReactiveFormsModule,

        UserRoutingModule,
        CoreModule
    ],
    declarations: [
        UserRootComponent,
        UserDashboardComponent,
        UserListsComponent,
        NewListComponent,
        UserListComponent,
        SearchComponent,
        UserProductListNoteComponent,
        ListShareSettingsComponent,
        ListOptionsComponent
    ]
})
export class UserModule {
}
