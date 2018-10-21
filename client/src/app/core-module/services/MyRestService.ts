import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { HttpClient } from "@angular/common/http";
import QueryString from "querystring";

export const PAGE_SIZE = 20;

export class PagedResult<T extends MyRestEntity> {

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
        readonly service: MyRestService<T>,
        readonly url: string
    ) {
        if (this.hasNext) {
            this.next = service.getPaged(url, number + 1, size)
        }

        this.pages = [];
        for (let i = 0; i < totalPages; i++) {
            this.pages[i] = service.getPaged(url, i, size)
        }

        if (this.hasPrevious) {
            this.previous = service.getPaged(url, number - 1, size)
        }
    }


    static wrapFromResponse<T extends MyRestEntity>(result: PagedResult<T>, service: MyRestService<T>, url: string): PagedResult<T> {
        return new PagedResult<T>(
            result.content,
            result.size,
            result.totalElements,
            result.number,
            result.totalPages,
            result.first,
            result.last,

            service,
            url
        );
    }

    get hasNext(): boolean {
        return !this.last;
    }

    get hasPrevious(): boolean {
        return !this.first
    }

}

export interface MyRestEntity {
    id: number
}

export class MyRestService<T extends MyRestEntity> {

    protected readonly resourcePath: string;

    constructor(
        resourcePath: string,
        protected httpClient: HttpClient
    ) {
        this.resourcePath = `/api/${resourcePath}`
    }

    create(entity: T): Observable<T> {
        return this.httpClient.post<T>(this.resourcePath, entity);
    }


    update(entity: T): Observable<T> {
        const url = `${this.resourcePath}/${entity.id}`;

        return this.httpClient.put<T>(url, entity);
    }

    getById(entityId: number): Observable<T> {
        const url = `${this.resourcePath}/${entityId}`;
        return this.httpClient.get<T>(url);
    }

    public getAll(): Observable<T[]> {
        const url = `${this.resourcePath}/all`
        return this.httpClient.get<T[]>(url);
    }

    public getAllPaged(page: number = 0, size: number = 20): Observable<PagedResult<T>> {
        return this.getPaged(this.resourcePath, page, size);
    }

    public getPaged(url: string, page: number = 0, size: number = 20): Observable<PagedResult<T>> {

        url = this.replaceQueryParameters(url, {page, size});

        return this.httpClient.get<PagedResult<T>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }

    private replaceQueryParameters(url: string, toReplace) {
        const hasParameters = url.indexOf('?') > 0;
        let params          = {};

        if (hasParameters) {
            params = QueryString.parse(url.substr(url.indexOf('?') + 1));
            url    = url.substring(0, url.indexOf('?'));
        }

        for (let key in toReplace) {
            if (!toReplace.hasOwnProperty(key)) {
                continue;
            }
            params[key] = toReplace[key];
        }

        return `${url}?${QueryString.stringify(params)}`;
    }

    public delete(entity: T): Observable<any> {
        const url = `${this.resourcePath}/${entity.id}`;

        return this.httpClient.delete<T>(url);
    }

    public searchByNameAndSortByName(name: string): Observable<PagedResult<T>> {
        const url = `${this.resourcePath}/search?name=${name}`;

        return this.httpClient.get<PagedResult<T>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }
}
