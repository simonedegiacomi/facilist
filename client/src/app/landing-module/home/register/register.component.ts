import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../../core-module/services/user.service";
import { NewPasswordComponent } from "../../../core-module/components/new-password/new-password.component";

const $ = window['jQuery'];


@Component({
    selector: 'landing-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    registerForm: FormGroup;

    emailAlreadyInUse   = false;
    registering         = false;

    constructor(
        private userService: UserService,
        formBuilder: FormBuilder
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

            privacy: new FormControl(null, [
                Validators.requiredTrue
            ]),
        })
    }

    ngOnInit() {
    }

    get firstName () { return this.registerForm.get('firstName'); }

    get lastName () { return this.registerForm.get('lastName'); }

    get email () { return this.registerForm.get('email'); }

    shouldShowErrors (field: AbstractControl): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    onSubmit () {
        this.emailAlreadyInUse = false;
        this.registering = true;

        const data = this.registerForm.value;
        data.password = data.passwords.password;

        this.userService.register(data).subscribe(
            _ => this.onRegistered(),
            error => this.onRegisterError(error)
        );
    }

    onRegistered () {
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
