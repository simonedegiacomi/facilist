import { Component, Input } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from "@angular/forms";

/**
 * Define a validation rule that checks if the password insert in the second field matches the one inserted in the first
 * @param passwordFieldName
 * @param confirmFieldName
 */
function confirmPassword(passwordFieldName: string, confirmFieldName: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {

        const password = control.get(passwordFieldName).value;
        const confirm = control.get(confirmFieldName).value;

        if (confirm === password) {
            return null; // no error
        }

        return {'confirmPassword': 'differentPasswords'};
    };
}

/**
 * Define a validation rule that checks if a string matches a specified regexp and return a specified error if not
 * @param name The name of the error to return
 * @param pattern The pattern that the string must match
 */
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

    /**
     * Creates the password reactive form group
     * @param formBuilder
     */
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
