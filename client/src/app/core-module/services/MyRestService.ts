import { HalOptions, Resource, ResourceArray, RestService } from "hal-4-angular";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { Injector } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { NetworkErrorsService } from "./network-errors.service";

export const PAGE_SIZE = 20;

export const sortByName: HalOptions = {
    sort: [{
        path: 'name',
        order: 'ASC'
    }]
};

export class PagedResult<T> {

    public readonly next: Observable<PagedResult<T>>;

    public readonly pages: Observable<PagedResult<T>>[];

    public readonly previous: Observable<PagedResult<T>>;

    constructor(
        public readonly content: T[],
        public readonly size: number,
        public readonly totalElements: number,
        public readonly number: number,
        public readonly totalPages: number,
        public readonly first: boolean,
        public readonly last: boolean,

        readonly service: MyRestService<T>
    ) {
        if (this.hasNext) {
            this.next = service.getAllPaged(number + 1, size)
        }

        this.pages = [];
        for (let i = 0; i < totalPages; i++) {
            this.pages[i] = service.getAllPaged(i, size)
        }

        if (this.hasPrevious) {
            this.previous = service.getAllPaged(number - 1, size)
        }
    }


    static wrapFromResponse<T>(result: PagedResult<T>, service: MyRestService<T>): PagedResult<T> {
        return new PagedResult<T>(
            result.content,
            result.size,
            result.totalElements,
            result.number,
            result.totalPages,
            result.first,
            result.last,

            service
        );
    }

    get hasNext(): boolean {
        return !this.last;
    }

    get hasPrevious(): boolean {
        return !this.first
    }

}

export class MyRestService<T> {

    private readonly resourcePath: string;

    constructor(
        resourcePath: string,
        protected httpClient: HttpClient,
        protected errorService: NetworkErrorsService
    ) {
        this.resourcePath = `/api/${resourcePath}`
    }

    public getAllPaged(page: number = 0, size: number = 20): Observable<PagedResult<T>> {

        const url = `${this.resourcePath}?page=${page}&size=${size}`;

        return this.httpClient.get<PagedResult<T>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this))
        );
    }
}
