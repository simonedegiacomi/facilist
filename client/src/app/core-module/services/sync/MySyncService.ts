import { StompService } from "@stomp/ng2-stompjs";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

export class MySyncService {

    constructor (
        protected stompService: StompService
    ) {}

    protected subscribe<T> (path: string): Observable<SyncEvent<T>> {
        return this.stompService.subscribe(path).pipe(
            map(message => JSON.parse(message.body))
        );
    }

}

export const EventTypes = {
    CREATED: "created",
    MODIFIED: "modified",
    DELETED: "deleted"
};

export class SyncEvent<T> {

    model: T;

    event: string;

}
