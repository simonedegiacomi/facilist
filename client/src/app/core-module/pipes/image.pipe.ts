import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe that converts an image id in it's url
 */
@Pipe({
    name: 'image'
})
export class ImagePipe implements PipeTransform {

    transform(value: any, args?: any): any {
        return `/api/images/${value}`;
    }

}
