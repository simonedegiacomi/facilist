import { ShoppingListCategory } from "./shopping-list-category";
import { MyRestEntity } from "../services/rest/MyRestService";
import { Product } from "./product";
import { User } from "./user";

export class ShoppingList implements MyRestEntity {

    id: number = null;

    name: string;

    description: string;

    icon: string;

    category: ShoppingListCategory = null;

    products: ShoppingListProduct[] = [];

    collaborations: ShoppingListCollaboration[] = [];

    invites: Invite[] = [];

    creator: User;

}

export class ShoppingListProduct implements MyRestEntity {

    id: number = null;

    image: string;

    quantity: number = 1;

    bought: boolean = false;

    note: string = null;

    constructor(
        public product: Product
    ) {
        this.image = product.icon;
    }
}

export const CollaborationsRoles = {
    BASIC: "BASIC",
    SOCIAL: "SOCIAL",
    ADMIN: "ADMIN"
};

export class ShoppingListCollaboration implements MyRestEntity {
    id: number = null;

    user: User;

    role: string;
}

export class Invite implements MyRestEntity {
    id: number = null;

    email: string;
}

export class ShoppingListPreview {
    id: number;
    name: string;
    description: string;
    icon: string;

    itemsCount: number;
    boughtItemsCount: number;

    shared: boolean;

    constructor(list: ShoppingList) {
        this.id          = list.id;
        this.name        = list.name;
        this.description = list.description;
        this.icon        = list.icon;

        this.itemsCount       = list.products.length;
        this.boughtItemsCount = list.products.filter(p => p.bought).length;

        this.shared = list.collaborations.length > 0
    }
}
