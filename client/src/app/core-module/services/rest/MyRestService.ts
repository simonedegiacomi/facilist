import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { HttpClient } from "@angular/common/http";
import QueryString from "querystring";

/**
 * Wrapper of a paged result thst exposes methods and attributes to known the page numbers and to navigate between pages
 */
export class PagedResult<T extends MyRestEntity> {

    /**
     * Observable of items in the next page
     */
    public readonly next: Observable<PagedResult<T>>;

    /**
     * Observable of items in the indexed pages
     */
    public readonly pages: Observable<PagedResult<T>>[];

    /**
     * Observable of items in the previous page
     */
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

/**
 * Base service that exposes methods that maps to HTTP methods to create, get, search, update and delete entities
 */
export class MyRestService<T extends MyRestEntity> {

    protected readonly resourcePath: string;

    constructor(
        resourcePath: string,
        protected httpClient: HttpClient
    ) {
        this.resourcePath = `/api/${resourcePath}`
    }

    /**
     * Sends a POST to the resource path with the specified entity as the body
     * @param entity Entity to send in the request body
     */
    create(entity: T): Observable<T> {
        return this.httpClient.post<T>(this.resourcePath, entity);
    }

    /**
     * Sends a PUT with the specified entity as the body
     * @param entity Entity to send in the request body
     */
    update(entity: T): Observable<T> {
        const url = `${this.resourcePath}/${entity.id}`;

        return this.httpClient.put<T>(url, entity);
    }

    /**
     * Sends a GET with the specified id to the resource path
     * @param entityId
     */
    getById(entityId: number): Observable<T> {
        const url = `${this.resourcePath}/${entityId}`;
        return this.httpClient.get<T>(url);
    }

    /**
     * Send a GET to retrieve all the entities
     */
    public getAll(): Observable<T[]> {
        return this.httpClient.get<T[]>(this.resourcePath);
    }

    /**
     * Send a GET to retrieve pages of entities and wraps the response in a PagedResult
     * @param page Number of page
     * @param size Max items in page
     */
    public getAllPaged(page: number = 0, size: number = 20): Observable<PagedResult<T>> {
        return this.getPaged(this.resourcePath, page, size);
    }

    /**
     * Send a get to the specified URL and wraps the result in a PagedResult
     * @param url
     * @param page
     * @param size
     */
    public getPaged(url: string, page: number = 0, size: number = 20): Observable<PagedResult<T>> {

        url = this.replaceQueryParameters(url, {page, size});

        return this.httpClient.get<PagedResult<T>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }

    /**
     * Utility function to replace query parameters in a url with query parameters
     * @param url URL with query parameters to replace
     * @param toReplace Map of new query parameters
     */
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

    /**
     * Sends a DELETE
     * @param entity
     */
    public delete(entity: T): Observable<any> {
        const url = `${this.resourcePath}/${entity.id}`;

        return this.httpClient.delete<T>(url);
    }

    /**
     * Sends a GET with the name in the filter and wraps the response in a PagedResult
     * @param name
     */
    public searchByNameAndSortByName(name: string): Observable<PagedResult<T>> {
        const url = `${this.resourcePath}/search?name=${name}`;

        return this.httpClient.get<PagedResult<T>>(url).pipe(
            map(result => PagedResult.wrapFromResponse(result, this, url))
        );
    }
}
