import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Resource } from "hal-4-angular";

export class User extends Resource {

    id: number;
    email: string;

    firstName: string;
    lastName: string;

    emailVerified: boolean;
    admin: boolean;

    constructor({id, emailVerified, firstName, lastName, _links, email, admin}) {
        super();

        this.id            = id;
        super._links        = _links;
        this.firstName     = firstName;
        this.lastName      = lastName;
        this.email         = email;
        this.emailVerified = emailVerified;
        this.admin         = admin;

    }

}
