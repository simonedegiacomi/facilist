import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface NotebookSheetButton {
    iconClass: string;
    title: string;
    onClick: Function;
}

@Component({
    selector: 'app-notebook-sheet',
    templateUrl: './notebook-sheet.component.html',
    styleUrls: ['./notebook-sheet.component.css']
})
export class NotebookSheetComponent implements OnInit {

    @Input()
    buttons: NotebookSheetButton[];

    @Input()
    title: string;

    @Input()
    headerIcon: string;
    @Output()
    headerIconChange = new EventEmitter<string>();

    constructor() {
    }

    ngOnInit() {
    }

    onImageChanged (imageId: string) {
        this.headerIconChange.emit(imageId);
    }

}
