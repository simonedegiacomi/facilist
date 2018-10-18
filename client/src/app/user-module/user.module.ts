import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from "./user-routing.module";
import { UserRootComponent } from './user-root.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserListsComponent } from './user-lists/user-lists.component';
import { FormsModule } from "@angular/forms";
import { ImagePipe } from "../core-module/pipes/image.pipe";
import { CoreModule } from "../core-module/core.module";
import { NewListComponent } from './user-lists/new-list/new-list.component';

@NgModule({
    imports: [
        FormsModule,
        CommonModule,

        UserRoutingModule,
        CoreModule
    ],
    declarations: [
        UserRootComponent,
        UserDashboardComponent,
        UserListsComponent,
        NewListComponent
    ]
})
export class UserModule {
}
