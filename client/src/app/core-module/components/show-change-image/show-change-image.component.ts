import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UploadService } from "../../services/upload.service";

@Component({
    selector: 'app-show-change-image',
    templateUrl: './show-change-image.component.html',
    styleUrls: ['./show-change-image.component.css']
})
export class ShowChangeImageComponent implements OnInit {

    @Input() image: string;

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
        this.uploadService.uploadImage(file).pipe(
            // TODO: Catch error
        ).subscribe(id => this.imageChange.emit(id));
    }

}
