import { Observable, throwError } from "rxjs";

export function  ifResponseCodeThen(code : number, contextualError: any): <T>(error: any) => Observable<T | any> {
    return(error: any) => throwError(error.status === code ? contextualError : error)
}
