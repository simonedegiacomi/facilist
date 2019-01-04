import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UploadService } from "../../services/rest/upload.service";

/**
 * Components that show an image and, when the image is editable, allow the user to change it clicking on it.
 */
@Component({
    selector: 'app-show-change-image',
    templateUrl: './show-change-image.component.html',
    styleUrls: ['./show-change-image.component.css']
})
export class ShowChangeImageComponent {

    /**
     * Image to show
     */
    @Input() image: string;

    /**
     * Wheter or not the image is editable
     */
    @Input() editable: boolean = true;

    /**
     * Emits event when the image changes. The event contain the new image id
     */
    @Output() imageChange = new EventEmitter<string>();

    constructor(
        private uploadService: UploadService
    ) { }

    onSelectFile(evt: Event) {
        const files = evt.target['files'];
        if (files.length < 0) {
            return;
        }

        const file = files[0];

        this.startUpload(file);
    }

    private startUpload (file: File) {
        this.uploadService.uploadImage(file).subscribe(id => this.imageChange.emit(id));
    }

}
