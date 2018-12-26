import { Component, OnInit } from '@angular/core';
import { AuthService, WRONG_CREDENTIALS } from "../../services/auth.service";
import { UserService } from "../../services/rest/user.service";
import { User } from "../../models/user";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { NewPasswordComponent } from "../new-password/new-password.component";
import { NotebookSheetButton } from "../notebook-sheet/notebook-sheet.component";
import { I18nService } from "../../services/i18n.service";

const $ = window['jQuery'];

@Component({
    selector: 'app-user-settings',
    templateUrl: './user-settings.component.html',
    styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent implements OnInit {

    buttons: NotebookSheetButton[] = [{
        iconClass: 'close-icon',
        title: 'Chiudi',
        onClick: () => $('#userSettingsModal').modal('hide')
    }];

    changeEmailForm: FormGroup;
    changePasswordForm: FormGroup;

    changePasswordError = false;
    emailRequestSent    = false;

    user: User;

    isSaving: boolean;

    constructor(
        private authService: AuthService,
        private userService: UserService,
        formBuilder: FormBuilder,
        private i18n: I18nService
    ) {
        this.changeEmailForm    = formBuilder.group({
            email: new FormControl(null, [
                Validators.required,
                Validators.email
            ])
        });
        this.changePasswordForm = formBuilder.group({
            currentPassword: new FormControl(),
            passwords: NewPasswordComponent.createPasswordsFormGroup(formBuilder),
        });
    }

    ngOnInit() {
        this.user = this.authService.user;
    }

    onImageChanged(imageId: string) {
        this.isSaving   = true;
        this.user.photo = imageId;
        this.userService.changeUserPhoto(this.user).subscribe(() => this.isSaving = false);
    }

    onChangePassword() {
        this.isSaving            = true;
        this.changePasswordError = false;
        const formValues         = this.changePasswordForm.value;

        this.userService.changeUserPassword({
            currentPassword: formValues.currentPassword,
            newPassword: formValues.passwords.password
        }).subscribe(
            () => {
                this.isSaving = false;
                this.changePasswordForm.reset();
            },
            error => this.onChangePasswordError(error)
        );
    }

    onChangePasswordError(error: any) {
        this.isSaving = false;
        if (error == WRONG_CREDENTIALS) {
            this.changePasswordError = true;
        }
    }

    onChangeEmail() {
        this.isSaving = true;

        this.userService.changeUserEmail(this.changeEmailForm.value.email)
            .subscribe(() => {
                this.isSaving         = false;
                this.emailRequestSent = true;
            });
    }
}
