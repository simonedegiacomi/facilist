import { HalOptions, Resource, ResourceArray, RestService } from "hal-4-angular";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { Injector } from "@angular/core";

export const PAGE_SIZE = 20;

export const sortByName: HalOptions = {
    sort: [{
        path: 'name',
        order: 'ASC'
    }]
};

export class PagedResult<T extends Resource> {

    public readonly number: number;
    public readonly pageCount: number;

    public readonly hasNext: boolean;
    public readonly next: Observable<PagedResult<T>>;

    public readonly pages: Observable<PagedResult<T>>[];

    public readonly hasPrevious: boolean;
    public readonly previous: Observable<PagedResult<T>>;


    constructor(
        public readonly results: T[],
        private readonly service: RestService<T>,
        private readonly type: { new(): T},
        resourceArray: ResourceArray<T> = service.resourceArray
    ) {
        this.number             = resourceArray.pageNumber;
        this.pageCount          = resourceArray.totalPages;

        this.hasPrevious        = resourceArray.prev_uri != null;
        this.previous           = this.wrapResourceArray(resourceArray.prev(type));

        this.pages              = this.buildArrayOfObservablePages();

        this.hasNext            = resourceArray.next_uri != null;
        this.next               = this.wrapResourceArray(resourceArray.next(type));
    }


    private wrapResult(observable: Observable<T[]>): Observable<PagedResult<T>> {
        return observable.pipe(
            map(results => new PagedResult(results, this.service, this.type))
        )
    }

    private buildArrayOfObservablePages (): Observable<PagedResult<T>>[] {
        const resourceArray     = this.service.resourceArray;
        const pages             = [];

        for (let i = 0; i < resourceArray.totalPages; i++) {
            pages[i] = this.wrapResourceArray(resourceArray.page(this.type, i));
        }

        return pages;
    }


    private wrapResourceArray(observable: Observable<ResourceArray<T>>): Observable<PagedResult<T>> {
        return observable.pipe(
            map(resourceArray => new PagedResult(resourceArray.result, this.service, this.type, resourceArray))
        )
    }
}

export class MyRestService<T extends Resource> extends RestService<T> {


    constructor(
        private _type: { new(): T },
        resource: string,
        injector: Injector
    ) {
        super(_type, resource, injector);
    }

    protected paginate(results: Observable<T[]>): Observable<PagedResult<T>> {
        return results.pipe(
            map(result => new PagedResult(result, this, this._type))
        );
    }


    getAllPaged (options?: HalOptions): Observable<PagedResult<T>> {
        return this.paginate(super.getAll(options));
    }

    getAllSortedByName(): Observable<T[]> {
        return this.getAll(sortByName);
    }

    getAllPagedSortedByName(): Observable<PagedResult<T>> {
        return this.getAllPaged(sortByName);
    }


    searchByNameAndSortByName(name: string) :Observable<PagedResult<T>> {
        return this.paginate(this.search('findByNameContainingIgnoreCase', {
            ...sortByName,
            params: [{
                key: 'name',
                value: name
            }]
        }));
    }
}
