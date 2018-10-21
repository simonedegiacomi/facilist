import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from "./services/auth.service";
import { NetworkErrorsService } from "./services/network-errors.service";
import { UserService } from "./services/user.service";
import { ImagePipe } from "./pipes/image.pipe";
import { ImagePickerComponent } from './components/show-change-image/image-picker/image-picker.component';
import { ShowChangeImageComponent } from './components/show-change-image/show-change-image.component';

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
        ImagePipe,
        ImagePickerComponent,
        ShowChangeImageComponent
    ],
    exports: [
        ImagePipe,
        ShowChangeImageComponent
    ]
})
export class CoreModule {
}
