import { Component, OnInit } from '@angular/core';
import { UploadService } from "../../../services/upload.service";

@Component({
    selector: 'app-image-picker',
    templateUrl: './image-picker.component.html',
    styleUrls: ['./image-picker.component.css']
})
export class ImagePickerComponent implements OnInit {

    constructor(
        private uploadService: UploadService
    ) {
    }

    ngOnInit() {
    }

}
