import { Component, EventEmitter, Input, Output } from '@angular/core';

/**
 * Custom stiled checkbox
 */
@Component({
    selector: 'app-checkbox',
    templateUrl: './checkbox.component.html',
    styleUrls: ['./checkbox.component.css']
})
export class CheckboxComponent {

    /**
     * Is the checkbox checked by default
     */
    @Input() checked: boolean;

    /**
     * Emits event when the check status changes
     */
    @Output() checkedChange = new EventEmitter<boolean>();

    /**
     * Label of the checkbox
     */
    @Input() label = "";


    toggleValue() {
        this.checked = !this.checked;
        this.checkedChange.emit(this.checked);
    }
}
