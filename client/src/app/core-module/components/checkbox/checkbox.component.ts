import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-checkbox',
  templateUrl: './checkbox.component.html',
  styleUrls: ['./checkbox.component.css']
})
export class CheckboxComponent implements OnInit {

  @Input() checked: boolean;
  @Input() label = "";
  @Output() checkedChange = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit() {
  }

  toggleValue() {
  	this.checked = !this.checked;
  	this.checkedChange.emit(this.checked);
  }
}
