import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { AngularHalModule } from "hal-4-angular";
import { EditorModule } from "@tinymce/tinymce-angular";
import { ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from './app.component';
import { AppRoutingModule } from "./app-routing.module";
import { LandingModule } from "./landing-module/landing.module";
import { CoreModule } from "./core-module/core.module";
import { ExternalConfigurationService } from "./hal-config";
import { ProductService } from "./core-module/services/product.service";
import { NetworkErrorDisplayComponent } from './network-error-display/network-error-display.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminModule } from "./admin-module/admin.module";
import { HttpInterceptorsProvider } from "./http-interceptors";
import { UserModule } from "./user-module/user.module";
import { ImagePipe } from "./core-module/pipes/image.pipe";


@NgModule({
    declarations: [
        AppComponent,
        NetworkErrorDisplayComponent,
        NavbarComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        ReactiveFormsModule,
        AngularHalModule.forRoot(),
        EditorModule,


        AppRoutingModule,

        CoreModule,
        LandingModule,
        AdminModule,
        UserModule
    ],
    providers: [
        {
            provide: 'ExternalConfigurationService',
            useClass: ExternalConfigurationService
        },

        HttpInterceptorsProvider,

        ProductService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
