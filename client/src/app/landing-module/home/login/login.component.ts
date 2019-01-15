import { Component, OnInit } from '@angular/core';
import { AuthService, EMAIL_NOT_VERIFIED_YET, WRONG_CREDENTIALS } from "../../../core-module/services/auth.service";
import { FormControl, FormGroup } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

@Component({
    selector: 'landing-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => $('#loginModal').modal('hide')
    }];

    loggingIn          = false;
    loginError: string = null;
    rememberMe         = false;

    // Export error constants to the template file
    WRONG_CREDENTIALS      = WRONG_CREDENTIALS;
    EMAIL_NOT_VERIFIED_YET = EMAIL_NOT_VERIFIED_YET;

    loginForm = new FormGroup({
        email: new FormControl(''),
        password: new FormControl('')
    });

    constructor(
        private auth: AuthService,
        private router: Router,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.route.queryParamMap.subscribe(params => {
            // Open the modal in the url there is the query parameter to open the register modal
            if (params.has('openLoginModal')) {
                const email = params.get('email');
                if (email != null) {
                    this.email.setValue(email);
                }

                $('#loginModal').modal('show');
            }
        });
    }

    onSubmit() {
        this.loginError = null;
        this.loggingIn  = true;

        const {email, password} = this.loginForm.value;
        this.auth.login(email, password, this.rememberMe).subscribe(
            () => this.onLoggedIn(),
            error => this.onError(error));
    }

    onLoggedIn() {
        this.loggingIn = false;
        $('#loginModal').modal('hide');
    }

    onError(error: any) {
        if (error == WRONG_CREDENTIALS || error == EMAIL_NOT_VERIFIED_YET) {
            this.loginError = error;
        }

        this.loggingIn = false;
    }

    get email() {
        return this.loginForm.get('email');
    }
}
