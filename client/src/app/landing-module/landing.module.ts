import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LandingRoutingModule } from "./landing-routing.module";
import { HomeComponent } from './home/home.component';
import { LoginComponent } from "./home/login/login.component";
import { ReactiveFormsModule } from "@angular/forms";
import { RegisterComponent } from './home/register/register.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { RecoverPasswordComponent } from './home/recover-password/recover-password.component';
import { CompleteRecoverPasswordComponent } from './complete-recover-password/complete-recover-password.component';
import { NewPasswordComponent } from './common/new-password/new-password.component';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        LandingRoutingModule
    ],
    declarations: [
        LoginComponent,
        HomeComponent,
        RegisterComponent,
        VerifyEmailComponent,
        RecoverPasswordComponent,
        CompleteRecoverPasswordComponent,
        NewPasswordComponent
    ]
})
export class LandingModule {
}
