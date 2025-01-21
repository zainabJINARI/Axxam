import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

export interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  { path: '/dashboard', title: 'Dashboard', icon: 'nc-bank', class: '' },
  {
    path: '/reservations',
    title: 'Reservation',
    icon: 'nc-diamond',
    class: '',
  },
  {
    path: '/announcements',
    title: 'Announcements',
    icon: 'nc-pin-3',
    class: '',
  },
  { path: '/user', title: 'Profile', icon: 'nc-single-02', class: '' },
];


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent implements OnInit {
  username: string | null | undefined;
  public menuItems: any[] | undefined;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.menuItems = ROUTES.filter((menuItem) => menuItem);
    this.username = this.authService.getUsername();
  }

  onLogout(): void {
    this.authService.logout();
  }
}
