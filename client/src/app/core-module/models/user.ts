import { MyRestEntity } from "../services/rest/MyRestService";

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
    photo: string;

    firstName: string;
    lastName: string;

    locale: string;

    emailVerified: boolean;
    role: string;

}
