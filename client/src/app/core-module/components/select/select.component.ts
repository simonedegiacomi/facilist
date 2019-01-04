import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface OptionWithName {
    name: string;
}

/**
 * Custom styled select input control
 */
@Component({
    selector: 'app-select',
    templateUrl: './select.component.html',
    styleUrls: ['./select.component.css']
})
export class SelectComponent {

    @Input()
    placeholder: string = "Categoria";

    @Input()
    values: OptionWithName[];

    @Input()
    selected: OptionWithName;

    @Output()
    selectedChange = new EventEmitter<OptionWithName>();

    onChange() {
        this.selectedChange.emit(this.selected);
    }

}
