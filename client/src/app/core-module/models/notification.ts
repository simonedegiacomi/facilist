import { User } from "./user";
import { MyRestEntity } from "../services/rest/MyRestService";


export class Notification implements MyRestEntity {

    id: number;

    sentAt: Date;

    seenAt: Date;

    target: User;

    message: string;

    icon: string;

    url: string;
}
