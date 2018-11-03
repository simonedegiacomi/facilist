import { MyRestEntity } from "../services/rest/MyRestService";

export class Product implements MyRestEntity {

    id: number = null;

    name: string;

    categoryId: number;

    icon: string = 'default-product-icon';


}
