import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ShoppingListProduct } from "../../../../models/shopping-list";

@Component({
    selector: 'user-product-list-note',
    templateUrl: './user-product-list-note.component.html',
    styleUrls: ['./user-product-list-note.component.css']
})
export class UserProductListNoteComponent {

    @Input() product: ShoppingListProduct;

    isEditing = false;

    @Output() noteChange = new EventEmitter();

    constructor() {
    }


    onSave(){
        this.isEditing = false;
        this.noteChange.emit();
    }

}
