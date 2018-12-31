import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from "@angular/forms";

function confirmPassword(passwordFieldName: string, confirmFieldName: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {

        const password = control.get(passwordFieldName).value;
        const confirm = control.get(confirmFieldName).value;

        if (confirm === password) {
            return null;
        }

        return {'confirmPassword': 'differentPasswords'};
    };
}

function namedPatter(name: string, pattern: RegExp): ValidatorFn {
    const error = {};
    error[name] = true;
    return (control: AbstractControl): {[key: string]: any} | null => {
        const match = pattern.test(control.value);
        return match ? null : error;
    };
}

const MIN_PASSWORD_LENGTH = 6;

@Component({
    selector: 'app-new-password',
    templateUrl: './new-password.component.html',
    styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent {

    @Input() passwords: FormGroup;

    static createPasswordsFormGroup (formBuilder: FormBuilder) {
        return formBuilder.group({
            password: new FormControl(null, [
                Validators.required,
                Validators.minLength(MIN_PASSWORD_LENGTH),
                namedPatter('mustContainANumber', /.*(?=.*\d).*/),
                namedPatter('mustContainASymbol', /.*(?=.*[#$@!%&*?]).*/)
            ]),

            confirmPassword: new FormControl(null),
        }, {
            validator: confirmPassword('password', 'confirmPassword')
        });
    }

    get password () { return this.passwords.get('password'); }

    get passwordConfirm () { return this.passwords.get('passwordConfirm'); }

    shouldShowErrors (field: AbstractControl): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    /**
     * Compute a percentage that aims to tell the user how much a password is strong
     */
    passwordScore () {
        const length = this.password.value != null ? this.password.value.length : 0;
        let percentage = Math.min(length * 5, 100);


        if (this.password.invalid) {
            percentage = Math.min(percentage, 15);
        }

        return `${percentage}%`;
    }

}
