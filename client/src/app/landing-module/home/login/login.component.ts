import { Component, OnInit } from '@angular/core';
import { AuthService, WRONG_CREDENTIALS } from "../../../core-module/services/auth.service";
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

    loggingIn = false;
    loginError = false;
    rememberMe = false;

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
        this.loginError = false;
        this.loggingIn = true;

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
        if (error == WRONG_CREDENTIALS) {
            this.loginError = true;
        }
        this.loggingIn = false;
    }

    get email () { return this.loginForm.get('email'); }
}
