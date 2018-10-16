import { Product } from "./product";

export class ProductCategory {

    id: number = -1; // TODO: Use null

    name: string;

    description: string;

    products: Product[];

    icon = 'default-product-category-icon';
}
