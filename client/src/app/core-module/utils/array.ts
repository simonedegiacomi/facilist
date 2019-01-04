import { Observable, Subject, throwError } from "rxjs";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";

export function  ifResponseCodeThen(code : number, contextualError: any): <T>(error: any) => Observable<T | any> {
    return(error: any) => throwError(error.status === code ? contextualError : error)
}


export function removeFromArrayIfPresent(array, item) {
    const index = array.indexOf(item);

    if (index >= 0) {
        array.splice(index, 1);
    }
}


export function removeFromArrayByIdIfPresent(array, id) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1);
    }
}


export function replaceArrayItemByIdIfPresent(array, id, newItem) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1, newItem);
    }
}


/**
 *
 * @param subject
 * @param then
 */
export function debounceAndDistinctThenMap <T, K> (subject: Subject<T>, then: (searchTerm: T) => Observable<K>) {
    return subject.pipe(
        debounceTime(300),

        distinctUntilChanged(),

        switchMap(searchTerm => then(searchTerm))
    );
}
