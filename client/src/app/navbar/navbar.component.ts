import { Component } from '@angular/core';
import { AuthService } from "../core-module/services/auth.service";
import { ActivatedRoute, Router } from "@angular/router";
import { map } from "rxjs/operators";
import { Roles } from "../core-module/models/user";
import { I18nService } from "../core-module/services/i18n.service";

const $ = window['jQuery'];

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

    applicationName = "Facilist";

    constructor(
        private auth: AuthService,
        private router: Router,
        private i18n: I18nService
    ) { }

    readonly user$      = this.auth.user$;
    readonly isGuest$   = this.user$.pipe(map(user => user == null));
    readonly isUser$    = this.user$.pipe(map(user => user != null && Roles.isUser(user)));
    readonly isAdmin$   = this.user$.pipe(map(user => user != null && Roles.isAdmin(user)));


    onClickLogout () {
        this.auth.logout().subscribe(
            _ => this.onLoggedOut()
        )
    }

    onLoggedOut () {
        this.router.navigate(['/home']);
    }

    get currentLocale () {
        return this.i18n.getCurrentLocale();
    }

    set currentLocale (locale: string) {
        this.i18n.setCurrentLocale(locale);
    }

    goToHomeAndOpenModal (modalId: string) {
        this.router.navigateByUrl('/').then(() => $(`#${modalId}`).modal('show'));
    }
}
