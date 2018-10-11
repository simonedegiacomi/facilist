import { Component, OnInit } from '@angular/core';
import { AuthService } from "../core-module/services/auth.service";
import { Router } from "@angular/router";
import { map } from "rxjs/operators";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {


    constructor(
        private auth: AuthService,
        private router: Router
    ) { }

    ngOnInit() { }

    readonly user$      = this.auth.user$;
    readonly isGuest$   = this.user$.pipe(map(user => user == null));
    readonly isUser$    = this.user$.pipe(map(user => user != null && !user.admin));
    readonly isAdmin$   = this.user$.pipe(map(user => user != null && user.admin));


    onClickLogout () {
        this.auth.logout().subscribe(
            _ => this.onLoggedOut()
        )
    }

    onLoggedOut () {
        this.router.navigate(['/home']);
    }
}
