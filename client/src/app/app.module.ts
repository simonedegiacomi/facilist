import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { EditorModule } from "@tinymce/tinymce-angular";
import { ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from './app.component';
import { AppRoutingModule } from "./app-routing.module";
import { LandingModule } from "./landing-module/landing.module";
import { CoreModule } from "./core-module/core.module";
import { ProductService } from "./core-module/services/rest/product.service";
import { NetworkErrorDisplayComponent } from './network-error-display/network-error-display.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminModule } from "./admin-module/admin.module";
import { HttpInterceptorsProvider } from "./http-interceptors";
import { UserModule } from "./user-module/user.module";
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { NotificationsComponent } from './navbar/notifications/notifications.component';
import { InfiniteScrollModule } from "ngx-infinite-scroll";

@NgModule({
    declarations: [
        AppComponent,
        NetworkErrorDisplayComponent,
        NavbarComponent,
        NotificationsComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        ReactiveFormsModule,
        EditorModule,
        ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),

        CoreModule,
        LandingModule,
        AdminModule,
        UserModule,
        AppRoutingModule, // When defining the modules, this MUST be the last, because it matches unknown paths (404)
    ],
    providers: [
        HttpInterceptorsProvider,

        ProductService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
