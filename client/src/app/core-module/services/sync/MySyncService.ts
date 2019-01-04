import { StompService } from "@stomp/ng2-stompjs";
import { Observable } from "rxjs";
import { filter, map } from "rxjs/operators";

/**
 * Sync service based on STOMP over websocket
 */
export class MySyncService {

    constructor(
        protected stompService: StompService
    ) { }

    protected subscribe<T>(path: string): Observable<SyncEvent<T>> {
        return this.stompService.subscribe(path).pipe(
            map(message => JSON.parse(message.body))
        );
    }

    /**
     * Subscribe to a topic or queue but and return an observable that will emit a new value only if events match the
     * specified type
     * @param path Path of the queue or topic
     * @param type Event type to filter
     */
    protected subscribeByEventTypeAndMapModel<T>(path: string, type: string): Observable<T> {
        return this.subscribe<T>(path).pipe(
            filter(event => event.event == type),
            map(event => event.model)
        )
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
