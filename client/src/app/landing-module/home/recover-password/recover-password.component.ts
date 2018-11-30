import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { AuthService, EMAIL_NOT_REGISTERED } from "../../../core-module/services/auth.service";
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";

const $ = window['jQuery'];

@Component({
    selector: 'landing-recover-password',
    templateUrl: './recover-password.component.html',
    styleUrls: ['./recover-password.component.css']
})
export class RecoverPasswordComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        title: 'chiudi',
        iconClass: 'close-icon',
        onClick: () => $('#recoverPasswordModal').modal('hide')
    }];


    recoverPasswordForm = new FormGroup({
        email: new FormControl(null, [
            Validators.required,
            Validators.email
        ])
    });
    recovering = false;
    wrongEmail = false;

    constructor(
        private auth: AuthService
    ) {
    }

    ngOnInit() {
    }

    get email () { return this.recoverPasswordForm.get('email'); }

    shouldShowErrors (field: AbstractControl): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    onSubmit () {
        this.recovering = true;
        this.wrongEmail = false;
        this.auth.recoverPassword(this.email.value).subscribe(
            _ => this.onEmailSent(),
            error => this.onError(error)
        );
    }

    onEmailSent () {
        this.recovering = false;

        $('.modal').modal('hide');
        $('#recoverEmailSentModal').modal('show');
    }

    onError (error: any) {
        if (error === EMAIL_NOT_REGISTERED) {
            this.wrongEmail = true;
        }
        this.recovering = false;
    }
}
