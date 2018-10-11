import { Resource } from "hal-4-angular";
import { ProductCategory } from "./product-category";

export class ShoppingListCategory extends Resource{

    id: number = null;

    name: string;

    description: string;

    icon: string = 'default-shopping-list-category-icon';

    productCategories: ProductCategory[];

    constructor() {
        super();
        this._links = {
            icon: {
                href: 'api/images/default-shopping-list-category-icon' // TODO: Should the model know the api call for the image???
            }
        }
    }
}
