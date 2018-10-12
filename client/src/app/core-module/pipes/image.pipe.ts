import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'image'
})
export class ImagePipe implements PipeTransform {

    transform(value: any, args?: any): any {
        return `/api/images/${value}`;
    }

}
