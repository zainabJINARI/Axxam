import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  notificationsVisible = true;
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

  toggleNotifications() {
    console.log('Notification is Clicked !!!!');
    this.notificationsVisible = !this.notificationsVisible;
  }
}
