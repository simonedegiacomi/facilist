import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'cookies-consent',
    templateUrl: './cookies-consent.component.html',
    styleUrls: ['./cookies-consent.component.css']
})
export class CookiesConsentComponent implements OnInit {

    showMessage: boolean;


    ngOnInit() {
        this.showMessage = this.messageHasBenHidden();
    }

    hideMessage () {
        this.rememberMessageHasBeenHidden();
        this.showMessage = false;
    }

    private messageHasBenHidden () {
        const preference = localStorage.getItem('cookieMessageHasBenHidden');

        return preference !== 'true';
    }

    private rememberMessageHasBeenHidden () {
        localStorage.setItem('cookieMessageHasBenHidden', 'true');
    }


}
