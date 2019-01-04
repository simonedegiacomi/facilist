import { Component, OnInit } from '@angular/core';
import { NetworkErrorsService } from "../core-module/services/network-errors.service";

const $ = window['jQuery'];

/**
 * Component that shows an alert with a description of the network error when a network or server error happen.
 */
@Component({
  selector: 'app-network-error-display',
  templateUrl: './network-error-display.component.html',
  styleUrls: ['./network-error-display.component.css']
})
export class NetworkErrorDisplayComponent implements OnInit {

  constructor(
      private networkErrorService: NetworkErrorsService
  ) { }

  ngOnInit() {
      this.networkErrorService.error.subscribe(_ => {
          $('.modal').modal('hide'); // Close other modals
          $('#errorModal').modal('show');
      })
  }

}
