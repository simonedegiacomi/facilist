import { ProductCategory } from "./product-category";
import { MyRestEntity } from "../services/rest/MyRestService";

export class ShoppingListCategory implements MyRestEntity {

    id: number = null;

    name: string;

    description: string;

    icon: string = 'default-shopping-list-category-icon';

    productCategories: ProductCategory[];
}
