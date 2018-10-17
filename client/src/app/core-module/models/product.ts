import { MyRestEntity } from "../services/MyRestService";

export class Product implements MyRestEntity {

    id: number = null;

    name: string;

    categoryId: number;

    icon: string = 'default-product-icon';


}
