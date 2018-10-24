import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from "./services/auth.service";
import { NetworkErrorsService } from "./services/network-errors.service";
import { UserService } from "./services/user.service";
import { ImagePipe } from "./pipes/image.pipe";
import { ImagePickerComponent } from './components/show-change-image/image-picker/image-picker.component';
import { ShowChangeImageComponent } from './components/show-change-image/show-change-image.component';
import { UserSettingsComponent } from "./components/user-settings/user-settings.component";
import { NewPasswordComponent } from "./components/new-password/new-password.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule
    ],
    providers: [
        AuthService,
        UserService,
        NetworkErrorsService
    ],
    declarations: [
        ImagePipe,
        ImagePickerComponent,
        ShowChangeImageComponent,
        UserSettingsComponent,
        NewPasswordComponent
    ],
    exports: [
        ImagePipe,
        ShowChangeImageComponent,
        UserSettingsComponent,
        NewPasswordComponent
    ]
})
export class CoreModule {
}
