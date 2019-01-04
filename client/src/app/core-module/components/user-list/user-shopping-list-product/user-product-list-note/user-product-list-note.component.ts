import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ShoppingListProduct } from "../../../../models/shopping-list";

/**
 * Show and edit notes of a product in a shopping list
 */
@Component({
    selector: 'user-product-list-note',
    templateUrl: './user-product-list-note.component.html',
    styleUrls: ['./user-product-list-note.component.css']
})
export class UserProductListNoteComponent {

    @Input() product: ShoppingListProduct;

    isEditing = false;

    @Output() noteChange = new EventEmitter();

    onSave(){
        this.isEditing = false;
        this.noteChange.emit();
    }

}
