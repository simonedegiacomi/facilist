import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from "./user-routing.module";
import { UserRootComponent } from './user-root.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserListsComponent } from './user-lists/user-lists.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CoreModule } from "../core-module/core.module";
import { NewListComponent } from './user-lists/new-list/new-list.component';
import { ListShareSettingsComponent } from './user-list-page/list-share-settings/list-share-settings.component';
import { ListOptionsComponent } from './user-list-page/list-options/list-options.component';
import { UserListPageComponent } from './user-list-page/user-list-page.component';
import { ChatComponent } from './user-list-page/chat/chat.component';
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { ConfirmDeleteListComponent } from './user-list-page/list-options/confirm-delete-list/confirm-delete-list.component';

@NgModule({
    imports: [
        FormsModule,
        CommonModule,
        ReactiveFormsModule,
        InfiniteScrollModule,

        UserRoutingModule,
        CoreModule,
    ],
    declarations: [
        UserRootComponent,
        UserDashboardComponent,
        UserListsComponent,
        NewListComponent,
        ListShareSettingsComponent,
        ListOptionsComponent,
        UserListPageComponent,
        ChatComponent,
        ConfirmDeleteListComponent
    ]
})
export class UserModule {
}
