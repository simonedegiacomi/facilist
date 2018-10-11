import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { AuthService } from "../../core-module/services/auth.service";

@Component({
    templateUrl: './verify-email.component.html',
    styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {

    verifying = false;
    verifyError = false;
    verified = false;

    redirectLoginQueryParams = {
        openLoginModal: true,
        email: null
    };

    constructor(
        private activatedRoute: ActivatedRoute,
        private authService: AuthService
    ) {
    }

    ngOnInit() {
        this.activatedRoute.queryParamMap.subscribe(_ =>this.onRouteChanged());
    }

    onRouteChanged() {
        const email = this.activatedRoute.snapshot.paramMap.get('email');
        const token = this.activatedRoute.snapshot.queryParamMap.get('token');

        this.verifyEmail(email, token);
    }

    verifyEmail(email: string, token: string) {
        this.verifying = true;
        this.verifyError = false;
        this.verified = false;

        this.authService.verifyEmail(email, token).subscribe(
            _ => this.onEmailVerified(email),
            error => this.onVerifyError(error)
        );
    }


    onEmailVerified(email: string) {
        this.verifying = false;
        this.verified = true;
        this.redirectLoginQueryParams.email = email;
    }

    onVerifyError(error: any) {
        this.verifying = false;
        this.verifyError = true;
    }
}
