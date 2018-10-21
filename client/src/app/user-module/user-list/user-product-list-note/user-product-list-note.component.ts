import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from "../../../core-module/models/product";
import { ShoppingListProduct } from "../../../core-module/models/shopping-list";

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
