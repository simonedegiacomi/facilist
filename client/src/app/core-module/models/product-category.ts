import { Resource } from "hal-4-angular";
import { Product } from "./product";

export class ProductCategory extends Resource {

    id: number = -1; // TODO: Use null

    name: string;

    description: string;

    products: Product[];

    icon = 'default-product-category-icon';

    constructor() {
        super();
        this._links = {
            icon: {
                href: 'api/images/default-product-category-icon' // TODO: Should the model know the api call for the image???
            }
        }
    }


}
