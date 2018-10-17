import { Resource } from "hal-4-angular";
import { ProductCategory } from "./product-category";

export class ShoppingListCategory extends Resource{

    id: number = null;

    name: string;

    description: string;

    icon: string = 'default-shopping-list-category-icon';

    productCategories: ProductCategory[];
}
