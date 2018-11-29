import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from "./services/auth.service";
import { NetworkErrorsService } from "./services/network-errors.service";
import { UserService } from "./services/rest/user.service";
import { ImagePipe } from "./pipes/image.pipe";
import { ImagePickerComponent } from './components/show-change-image/image-picker/image-picker.component';
import { ShowChangeImageComponent } from './components/show-change-image/show-change-image.component';
import { UserSettingsComponent } from "./components/user-settings/user-settings.component";
import { NewPasswordComponent } from "./components/new-password/new-password.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { StompConfig, StompService } from "@stomp/ng2-stompjs";
import { stompConfig } from "./stompConfig";
import { UserListComponent } from "./components/user-list/user-list.component";
import { RouterModule } from "@angular/router";
import { SearchComponent } from "./components/user-list/search/search.component";
import { UserProductListNoteComponent } from "./components/user-list/user-shopping-list-product/user-product-list-note/user-product-list-note.component";
import { UserShoppingListProductComponent } from "./components/user-list/user-shopping-list-product/user-shopping-list-product.component";
import { NewUserProductComponent } from './components/user-list/new-user-product/new-user-product.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { SelectComponent } from './components/select/select.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule
    ],
    providers: [
        AuthService,
        UserService,
        NetworkErrorsService,
        StompService,
        {
            provide: StompConfig,
            useValue: stompConfig
        }
    ],
    declarations: [
        ImagePipe,
        ImagePickerComponent,
        ShowChangeImageComponent,
        UserSettingsComponent,
        NewPasswordComponent,
        UserListComponent,
        SearchComponent,
        UserProductListNoteComponent,
        UserShoppingListProductComponent,
        NewUserProductComponent,
        CheckboxComponent,
        SelectComponent,
    ],
    exports: [
        ImagePipe,
        ShowChangeImageComponent,
        UserSettingsComponent,
        NewPasswordComponent,
        UserListComponent,
        CheckboxComponent,
        SelectComponent
    ]
})
export class CoreModule {
}
