import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MyRestEntity, PagedResult } from "../../services/rest/MyRestService";
import { Observable } from "rxjs";

/**
 * Component that show link to pages and load them when the user clicks on the link
 */
@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.css']
})
export class PaginationComponent<T extends MyRestEntity> {

    /**
     * Page to show
     */
    @Input() page: PagedResult<T>;

    /**
     * Emits events when the user changes page
     */
    @Output() pageChange = new EventEmitter<PagedResult<T>>();

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
