import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  notifications = [
    'Nouvelle notification 1',
    'Nouvelle notification 2',
    'Nouvelle notification 3',
    'Nouvelle notification 4',
    'Nouvelle notification 5',
    'Nouvelle notification 6',
    'Nouvelle notification 7',
    'Nouvelle notification 8',
  ];

  @Output() updateValue = new EventEmitter<void>();

  changeIsShown() {
    this.updateValue.emit(); // No argument is passed
  }
 
}
