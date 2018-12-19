import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LandingRoutingModule } from "./landing-routing.module";
import { HomeComponent } from './home/home.component';
import { LoginComponent } from "./home/login/login.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RegisterComponent } from './home/register/register.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { RecoverPasswordComponent } from './home/recover-password/recover-password.component';
import { CompleteRecoverPasswordComponent } from './complete-recover-password/complete-recover-password.component';
import { CoreModule } from "../core-module/core.module";
import { TryNowComponent } from './try-now/try-now.component';
import { UserModule } from "../user-module/user.module";
import { NewDemoListComponent } from './try-now/new-demo-list/new-demo-list.component';
import { CookiesConsentComponent } from './home/cookies-consent/cookies-consent.component';

@NgModule({
    imports: [
        CommonModule,
        CoreModule,
        ReactiveFormsModule,
        LandingRoutingModule,
        UserModule,
        FormsModule
    ],
    declarations: [
        LoginComponent,
        HomeComponent,
        RegisterComponent,
        VerifyEmailComponent,
        RecoverPasswordComponent,
        CompleteRecoverPasswordComponent,
        TryNowComponent,
        NewDemoListComponent,
        CookiesConsentComponent
    ]
})
export class LandingModule {
}
