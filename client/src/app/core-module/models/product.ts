import { MyRestEntity } from "../services/rest/MyRestService";
import { ProductCategory } from "./product-category";

export const defaultProductIcon = "default-product-category-icon";


export class Product implements MyRestEntity {

    id: number = null;

    name: string;

    categoryId: number;

    icon: string = defaultProductIcon;

    category: ProductCategory;

}
