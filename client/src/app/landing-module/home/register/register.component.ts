import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../../core-module/services/rest/user.service";
import { NewPasswordComponent } from "../../../core-module/components/new-password/new-password.component";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";
import { ActivatedRoute } from "@angular/router";
import { I18nService } from "../../../core-module/services/i18n.service";

const $ = window['jQuery'];


@Component({
    selector: 'landing-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => $('#registerModal').modal('hide')
    }];

    showPrivacyContract = false;

    registerForm: FormGroup;

    emailAlreadyInUse = false;
    registering       = false;
    privacy           = false;

    constructor(
        private userService: UserService,
        formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private i18n: I18nService
    ) {
        this.registerForm = formBuilder.group({
            firstName: new FormControl(null, [
                Validators.required
            ]),

            lastName: new FormControl(null, [
                Validators.required
            ]),

            passwords: NewPasswordComponent.createPasswordsFormGroup(formBuilder),

            email: new FormControl(null, [
                Validators.required,
                Validators.email
            ]),
        })
    }

    ngOnInit() {
        this.route.queryParamMap.subscribe(params => {
            if (params.has('openRegisterModal')) {
                $('#registerModal').modal('show');
            }
        });
    }

    get firstName() {
        return this.registerForm.get('firstName');
    }

    get lastName() {
        return this.registerForm.get('lastName');
    }

    get email() {
        return this.registerForm.get('email');
    }

    shouldShowErrors(field: AbstractControl): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    onSubmit() {
        this.emailAlreadyInUse = false;
        this.registering       = true;

        const data    = this.registerForm.value;
        data.password = data.passwords.password;

        this.userService.register({
            ...this.registerForm.value,
            password: data.passwords.password,
            locale: this.i18n.getCurrentLocale()
        }).subscribe(
            _ => this.onRegistered(),
            error => this.onRegisterError(error)
        );
    }

    onRegistered() {
        this.registering = false;

        $('#registerModal').modal('hide');
        $('#registeredModal').modal('show');
    }

    onRegisterError(error: any) {
        this.registering = false;
        if (error == 'emailAlreadyInUse') {
            this.emailAlreadyInUse = true;
        }
    }


}
