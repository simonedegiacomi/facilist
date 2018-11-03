import { MyRestEntity } from "../services/rest/MyRestService";
import { User } from "./user";

export class ChatMessage implements MyRestEntity {

    id: number;

    user: User;

    message: string;

    sentAt: Date;

}
