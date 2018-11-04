import { MyRestEntity } from "../services/rest/MyRestService";
import { ProductCategory } from "./product-category";

export class Product implements MyRestEntity {

    id: number = null;

    name: string;

    categoryId: number;

    icon: string;

    category: ProductCategory;

}
