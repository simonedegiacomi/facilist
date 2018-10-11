import { Resource } from "hal-4-angular";
import { ProductCategory } from "./product-category";

export class Product extends Resource {

    id: number = null;

    name: string;

    category: ProductCategory; // TODO: Provare a vedere se funziona senza 'string'

    icon: string = 'default-product-icon';

    constructor() {
        super();
        this._links = {
            icon: {
                href: 'api/images/default-product-icon' // TODO: Should the model know the api call for the image???
            },
            photo: {
                href: 'api/images/default-product-photo' // TODO: Should the model know the api call for the image???
            }
        }
    }


}
