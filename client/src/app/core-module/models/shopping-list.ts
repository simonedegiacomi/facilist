import { Resource } from "hal-4-angular";
import { ShoppingListCategory } from "./shopping-list-category";

export class ShoppingList extends Resource {
    id: number = null;

    name: string;

    description: string;

    icon: string;

    category: ShoppingListCategory;
}
