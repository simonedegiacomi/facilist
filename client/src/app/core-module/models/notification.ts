import { ShoppingList, ShoppingListCollaboration } from "./shopping-list";
import { User } from "./user";
import { Product } from "./product";
import { MyRestEntity } from "../services/MyRestService";


export class Notification implements MyRestEntity {

    id: number;

    type: string;

    shoppingList: ShoppingList;

    sentAt: Date;

    seenAt: Date;

    source: User;

    target: User;

}

export class ShoppingListCollaborationNotification extends Notification {

    action: string;

    collaboration: ShoppingListCollaboration;

}

export class ShoppingListNotification extends Notification {

    action: string;

}

export class ShoppingListProductNotification extends Notification {
    changes: ShoppingListProductChange[]
}

export class ShoppingListProductChange {

    id: number;

    type: string;

    product: Product;
}
