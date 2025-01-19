import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  notificationsVisible = false;
  toggleNotifications() {
    console.log('Notification is Clicked !!!!');
    this.notificationsVisible = !this.notificationsVisible;
  }

}
