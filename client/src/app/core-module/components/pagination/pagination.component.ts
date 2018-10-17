import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MyRestEntity, PagedResult } from "../../services/MyRestService";
import { Observable } from "rxjs";

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.css']
})
export class PaginationComponent<T extends MyRestEntity> implements OnInit {

    @Input() page: PagedResult<T>;
    @Output() pageChange = new EventEmitter<PagedResult<T>>();

    constructor() {
    }

    ngOnInit() {
    }

    loadPage (page: Observable<PagedResult<T>>) {
        this.page = null;
        page.subscribe(page => {
            this.page = page;
            this.pageChange.emit(page);
        });
    }

    reloadCurrentPage () {
        this.loadPage(this.page.pages[this.page.number]);
    }

}
