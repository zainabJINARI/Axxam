import { Component, OnInit } from '@angular/core';

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

// { path: '/table',         title: 'Table List',        icon:'nc-tile-56',    class: '' },
// { path: '/typography',    title: 'Typography',        icon:'nc-caps-small', class: '' },
// { path: '/upgrade',       title: 'Upgrade to PRO',    icon:'nc-spaceship',  class: 'active-pro' },
// { path: '/notifications', title: 'Profile',     icon:'nc-bell-55',    class: '' },

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent implements OnInit {
  public menuItems: any[] | undefined;

  ngOnInit() {
    this.menuItems = ROUTES.filter((menuItem) => menuItem);
  }
}
