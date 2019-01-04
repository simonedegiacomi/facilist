import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { NewPasswordComponent } from "../../core-module/components/new-password/new-password.component";
import { ActivatedRoute } from "@angular/router";
import { AuthService, INVALID_CODE } from "../../core-module/services/auth.service";

@Component({
    selector: 'app-complete-recover-password',
    templateUrl: './complete-recover-password.component.html',
    styleUrls: ['./complete-recover-password.component.css']
})
export class CompleteRecoverPasswordComponent {

    passwords: FormGroup;

    recovering = false;
    passwordChanged = false;
    invalidCode = false;

    constructor(
        formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private auth: AuthService
    ) {
        this.passwords = NewPasswordComponent.createPasswordsFormGroup(formBuilder)
    }

    get email () { return this.route.snapshot.paramMap.get('email'); }

    get token () { return this.route.snapshot.queryParamMap.get('token'); }


    onSubmit () {
        this.recovering = true;
        this.invalidCode = false;

        const data = {
            email: this.email,
            newPassword: this.passwords.value.password,
            token: this.token
        };

        this.auth.completeRecoverPassword(data).subscribe(
            _ => this.onPasswordChanged(),
            error => this.onError(error)
        )
    }

    onPasswordChanged () {
        this.recovering = false;
        this.passwordChanged = true;
    }

    onError (error: any) {
        if (error === INVALID_CODE) {
            this.invalidCode = true;
        }
        this.recovering = false;
    }
}
