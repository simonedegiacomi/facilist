import { Component } from '@angular/core';
import { NotebookSheetButton } from "../../../core-module/components/notebook-sheet/notebook-sheet.component";
import { Router } from "@angular/router";

const $ = window['jQuery'];

/**
 * Components that shows a dialog that invites the user to register
 */
@Component({
    selector: 'invite-to-subscribe',
    templateUrl: './invite-to-subscribe.component.html',
    styleUrls: ['./invite-to-subscribe.component.css']
})
export class InviteToSubscribeComponent {

    buttons: NotebookSheetButton[] = [{
        title: 'Chiudi',
        iconClass: 'close-icon',
        onClick: () => this.closeModal()
    }];

    constructor(private router: Router) { }

    register() {
        this.closeModal();

        //  Redirect to the home woth the registration modal open
        this.router.navigateByUrl('/home?openRegisterModal=true');
    }

    closeModal() {
        $('#inviteToSubscribe').modal('hide');
    }

}
