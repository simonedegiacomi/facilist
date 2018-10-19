import { ShoppingListCategory } from "./shopping-list-category";
import { MyRestEntity } from "../services/MyRestService";
import { Product } from "./product";

export class ShoppingList implements MyRestEntity {

    id: number = null;

    name: string;

    description: string;

    icon: string;

    category: ShoppingListCategory;

    products: ShoppingListProduct[];
}

export class ShoppingListProduct implements MyRestEntity {

    id: number = null;

    product: Product;

    quantity: number;

    toBuy: boolean;

    note: string;

    image: string;
}
