import { Observable, throwError } from "rxjs";

/**
 * Pipe function that maps a specified HTTP status code to an error
 * @param code
 * @param contextualError
 */
export function  ifResponseCodeThen(code : number, contextualError: any): <T>(error: any) => Observable<T | any> {
    return(error: any) => throwError(error.status === code ? contextualError : error)
}
