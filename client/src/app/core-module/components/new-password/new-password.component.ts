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


@Component({
    selector: 'app-new-password',
    templateUrl: './new-password.component.html',
    styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {

    @Input() passwords: FormGroup;

    static createPasswordsFormGroup (formBuilder: FormBuilder) {
        return formBuilder.group({
            password: new FormControl(null, [
                Validators.required,
                Validators.minLength(6), // TODO: Extract constant
                namedPatter('mustContainANumber', /.*(?=.*\d).*/),
                namedPatter('mustContainASymbol', /.*(?=.*[#$@!%&*?]).*/)
            ]),

            confirmPassword: new FormControl(null),
        }, {
            validator: confirmPassword('password', 'confirmPassword')
        });
    }


    ngOnInit() {
    }

    get password () { return this.passwords.get('password'); }

    get passwordConfirm () { return this.passwords.get('passwordConfirm'); }

    shouldShowErrors (field: AbstractControl): boolean {
        return field.invalid && (field.dirty || field.touched);
    }


    passwordScore () {
        let percentage = Math.min(this.password.value.length * 5, 100);


        if (this.password.invalid) {
            percentage = Math.min(percentage, 15);
        }

        return `${percentage}%`;
    }

}
