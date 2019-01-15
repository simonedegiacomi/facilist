import { MyRestEntity } from "../services/rest/MyRestService";
import { ProductCategory } from "./product-category";
import { User } from "./user";

export const defaultProductIcon = "default-product-category-icon";


export class Product implements MyRestEntity {

    id: number = null;

    name: string;

    creator: User;

    categoryId: number;

    icon: string = defaultProductIcon;

    category: ProductCategory;

}
