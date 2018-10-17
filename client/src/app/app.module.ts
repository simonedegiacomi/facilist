import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { EditorModule } from "@tinymce/tinymce-angular";
import { ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from './app.component';
import { AppRoutingModule } from "./app-routing.module";
import { LandingModule } from "./landing-module/landing.module";
import { CoreModule } from "./core-module/core.module";
import { ProductService } from "./core-module/services/product.service";
import { NetworkErrorDisplayComponent } from './network-error-display/network-error-display.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminModule } from "./admin-module/admin.module";
import { HttpInterceptorsProvider } from "./http-interceptors";
import { UserModule } from "./user-module/user.module";


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
        EditorModule,


        AppRoutingModule,

        CoreModule,
        LandingModule,
        AdminModule,
        UserModule
    ],
    providers: [
        HttpInterceptorsProvider,

        ProductService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
