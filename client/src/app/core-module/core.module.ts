import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from "./services/auth.service";
import { NetworkErrorsService } from "./services/network-errors.service";
import { UserService } from "./services/user.service";

@NgModule({
    imports: [
        CommonModule
    ],
    providers: [
        AuthService,
        UserService,
        NetworkErrorsService
    ]
})
export class CoreModule {
}
