import { Component, OnInit } from '@angular/core';
import { RealtimeUpdatesService } from "../../core-module/services/realtime-updates.service";


@Component({
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    constructor(
        private realtime: RealtimeUpdatesService
    ) {
    }

    ngOnInit() {

    }


}
