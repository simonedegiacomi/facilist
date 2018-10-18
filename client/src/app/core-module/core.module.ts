import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from "./services/auth.service";
import { NetworkErrorsService } from "./services/network-errors.service";
import { UserService } from "./services/user.service";
import { ImagePipe } from "./pipes/image.pipe";

@NgModule({
    imports: [
        CommonModule
    ],
    providers: [
        AuthService,
        UserService,
        NetworkErrorsService
    ],
    declarations: [
        ImagePipe
    ],
    exports: [
        ImagePipe
    ]
})
export class CoreModule {
}
