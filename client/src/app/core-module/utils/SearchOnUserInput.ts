import { Observable, Subject, Subscription } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";

/**
 * Class that it's used to handle keyboard events on input field to search items.
 * This class handle the debouncing and distincion of user input to send search requests only when it's needed.
 */
export class SearchOnUserInput<T> {

    private subject: Subject<string> = new Subject();

    private readonly resultSubscription: Subscription;

    /**
     * @param onUserInputChanges Function that return an observable of the search results given a search tem
     * @param onResults Function called when the results are available
     */
    constructor(
        private onUserInputChanges: (searchTerm: string) => Observable<T>,
        private onResults: (results: T) => void
    ) {
        this.resultSubscription = this.subject.pipe(
            debounceTime(300),

            distinctUntilChanged(),

            switchMap(searchTerm => onUserInputChanges(searchTerm))
        ).subscribe(results => onResults(results));
    }

    unbind () {
        this.resultSubscription.unsubscribe();
    }

    onInput(input: string) {
        this.subject.next(input);
    }
}
