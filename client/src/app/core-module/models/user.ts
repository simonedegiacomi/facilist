import { MyRestEntity } from "../services/MyRestService";

export const Roles = {
    USER: "ROLE_USER",
    ADMIN: "ROLE_ADMIN",

    isAdmin(user: User) {
        return user.role === this.ADMIN;
    },

    isUser(user: User) {
        return user.role == this.USER
    }
};


export class User implements MyRestEntity {

    id: number;
    email: string;

    firstName: string;
    lastName: string;

    emailVerified: boolean;
    role: string;

    constructor({id, emailVerified, firstName, lastName, email, role}) {

        this.id            = id;
        this.firstName     = firstName;
        this.lastName      = lastName;
        this.email         = email;
        this.emailVerified = emailVerified;
        this.role          = role;

    }

}
