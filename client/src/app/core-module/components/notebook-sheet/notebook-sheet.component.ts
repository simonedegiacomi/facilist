import { Component, EventEmitter, Input, Output } from '@angular/core';

/**
 * Define a button that can appear on the sheet
 */
export interface NotebookSheetButton {
    /**
     * Icon of the button
     */
    iconClass: string;

    /**
     * Title of the button
     */
    title: string;

    /**
     * Function called when the user clicks on the button
     */
    onClick: Function;

    /**
     * If this function is not null, it will be called to check if the button should be visible at the moment.
     * If this function is null, the button will be always visible
     */
    shouldBeVisible?: Function;
}

/**
 * Component that wraps its content in the a div that is styled as a notebook sheet
 */
@Component({
    selector: 'app-notebook-sheet',
    templateUrl: './notebook-sheet.component.html',
    styleUrls: ['./notebook-sheet.component.css']
})
export class NotebookSheetComponent {

    /**
     * Buttons to show on the top right corner
     */
    @Input()
    buttons: NotebookSheetButton[] = [];

    /**
     * Title to put in the header
     */
    @Input()
    title: string;

    /**
     * Icon in the header
     */
    @Input()
    headerIcon: string;

    /**
     * Emits the event when the header icon is changes. The event contain the id of the new icon
     */
    @Output()
    headerIconChange = new EventEmitter<string>();

    /**
     * Make the header icon editable or not
     */
    @Input()
    headerIconEditable: boolean = true;

    onImageChanged(imageId: string) {
        this.headerIconChange.emit(imageId);
    }

    /**
     * Return the list of buttons currently visibile
     */
    get visibleButtons(): NotebookSheetButton[] {
        return this.buttons.filter(button => {
            if (button.shouldBeVisible != null) {
                return button.shouldBeVisible();
            }

            return true;
        });
    }
}
