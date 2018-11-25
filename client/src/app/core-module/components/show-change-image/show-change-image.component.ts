import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UploadService } from "../../services/rest/upload.service";

@Component({
    selector: 'app-show-change-image',
    templateUrl: './show-change-image.component.html',
    styleUrls: ['./show-change-image.component.css']
})
export class ShowChangeImageComponent implements OnInit {

    @Input() image: string;

    @Input() editable: boolean = true;

    @Output() imageChange = new EventEmitter<string>();

    constructor(
        private uploadService: UploadService
    ) {
    }

    ngOnInit() {
    }

    onSelectFile(evt: Event) {
        const files = evt.target['files'];
        if (files.length < 0) {
            return;
        }

        const file = files[0];

        this.startUpload(file);
    }

    startUpload (file: File) {
        this.uploadService.uploadImage(file).subscribe(id => this.imageChange.emit(id));
    }

}
