import { Component } from '@angular/core';
import { ShoppingListService } from "../core-module/services/rest/shopping-list.service";
import { BackendShoppingListService } from "./services/backend-shopping-list.service";

@Component({
    templateUrl: './user-root.component.html',
    styleUrls: ['./user-root.component.css'],
    providers: [{
        provide: ShoppingListService,
        useClass: BackendShoppingListService
    }]
})
export class UserRootComponent {

}
