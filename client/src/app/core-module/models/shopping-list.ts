import { ShoppingListCategory } from "./shopping-list-category";
import { MyRestEntity } from "../services/MyRestService";

export class ShoppingList implements MyRestEntity {
    id: number = null;

    name: string;

    description: string;

    icon: string;

    category: ShoppingListCategory;
}
