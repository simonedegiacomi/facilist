import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from "./user-routing.module";
import { UserRootComponent } from './user-root.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserListsComponent } from './user-lists/user-lists.component';
import { FormsModule } from "@angular/forms";

@NgModule({
    imports: [
        FormsModule,
        CommonModule,

        UserRoutingModule
    ],
    declarations: [UserRootComponent, UserDashboardComponent, UserListsComponent]
})
export class UserModule {
}
