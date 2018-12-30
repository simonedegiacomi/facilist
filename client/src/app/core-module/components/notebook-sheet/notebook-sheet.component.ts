import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface NotebookSheetButton {
    iconClass: string;
    title: string;
    onClick: Function;
    shouldBeVisible?: Function;
}

@Component({
    selector: 'app-notebook-sheet',
    templateUrl: './notebook-sheet.component.html',
    styleUrls: ['./notebook-sheet.component.css']
})
export class NotebookSheetComponent {

    @Input()
    buttons: NotebookSheetButton[] = [];

    @Input()
    title: string;

    @Input()
    headerIcon: string;
    @Output()
    headerIconChange = new EventEmitter<string>();

    onImageChanged(imageId: string) {
        this.headerIconChange.emit(imageId);
    }

    get visibleButtons(): NotebookSheetButton[] {
        return this.buttons.filter(button => {
            if (button.shouldBeVisible != null) {
                return button.shouldBeVisible();
            }

            return true;
        });
    }

}
