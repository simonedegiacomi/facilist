import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { NotLoggedInGuard } from "../core-module/services/guards/not-logged-in-guard.service";
import { VerifyEmailComponent } from "./verify-email/verify-email.component";
import { CompleteRecoverPasswordComponent } from "./complete-recover-password/complete-recover-password.component";
import { TryNowComponent } from "./try-now/try-now.component";

const routes: Routes = [
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [
            NotLoggedInGuard
        ]
    }, {
        path: 'tryNow',
        component: TryNowComponent,
        canActivate: [
            NotLoggedInGuard
        ]
    }, {
        path: 'verifyEmail/:email',
        component: VerifyEmailComponent,
        canActivate: [
            NotLoggedInGuard
        ],
        data: {
            verifyFirstEmail: true
        }
    }, {
        path: 'verifyNewEmail/:email',
        component: VerifyEmailComponent,
        data: {
            verifyFirstEmail: false
        }
    }, {
        path: 'recoverPassword/:email',
        component: CompleteRecoverPasswordComponent,
        canActivate: [
            NotLoggedInGuard
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ],
    providers: [
        NotLoggedInGuard
    ]
})
export class LandingRoutingModule {
}
