import { ShoppingListCategory } from "./shopping-list-category";
import { MyRestEntity } from "../services/MyRestService";
import { Product } from "./product";
import { User } from "./user";

export class ShoppingList implements MyRestEntity {

    id: number = null;

    name: string;

    description: string;

    icon: string;

    category: ShoppingListCategory;

    products: ShoppingListProduct[];

    collaborations: ShoppingListCollaboration[];

    invites: Invite[];

    creator: User;
}

export class ShoppingListProduct implements MyRestEntity {

    id: number = null;

    image: string;

    quantity: number = 1;

    bought: boolean = true;

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
