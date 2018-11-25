import { Product } from "./product";
import { MyRestEntity } from "../services/rest/MyRestService";

export class ProductCategory implements MyRestEntity{

    id: number = -1;

    name: string;

    description: string;

    products: Product[];

    icon = 'default-product-category-icon';
}
