import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { AuthService } from "../../core-module/services/auth.service";
import { UserService } from "../../core-module/services/rest/user.service";
import { Observable } from "rxjs";

@Component({
    templateUrl: './verify-email.component.html',
    styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {

    verifying   = false;
    verifyError = false;
    verified    = false;

    redirectLoginQueryParams = {
        openLoginModal: true,
        email: null
    };

    constructor(
        private activatedRoute: ActivatedRoute,
        private authService: AuthService,
        private userService: UserService
    ) {
    }

    ngOnInit() {
        this.activatedRoute.queryParamMap.subscribe(_ => this.onRouteChanged());
    }

    onRouteChanged() {
        const email = this.activatedRoute.snapshot.paramMap.get('email');
        const token = this.activatedRoute.snapshot.queryParamMap.get('token');

        this.verifyEmail(email, token);
    }

    verifyEmail(email: string, token: string) {
        this.verifying   = true;
        this.verifyError = false;
        this.verified    = false;

        let observable: Observable<any>;
        if (this.isVerifyingFirstEmailAddress) {
            observable = this.authService.verifyEmail(email, token);
        } else {
            observable = this.userService.verifyNewEmail(email, token);
        }

        observable.subscribe(
            _ => this.onEmailVerified(email),
            error => this.onVerifyError(error)
        );
    }

    get isVerifyingFirstEmailAddress() {
        return this.activatedRoute.snapshot.data.verifyFirstEmail;
    }


    onEmailVerified(email: string) {
        this.verifying                      = false;
        this.verified                       = true;
        this.redirectLoginQueryParams.email = email;
    }

    onVerifyError(error: any) {
        this.verifying   = false;
        this.verifyError = true;
    }
}
