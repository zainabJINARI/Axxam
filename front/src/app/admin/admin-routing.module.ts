import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './admin-components/dashboard/dashboard.component';
import { ReservationComponent } from './admin-components/reservation/reservation.component';
import { AdminComponent } from './admin.component';
import { AnnoucementComponent } from './admin-components/annoucement/annoucement.component';
import { AddAnnounceComponent } from './admin-components/add-announce/add-announce.component';
import { EditProfileComponent } from './admin-components/edit-profile/edit-profile.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'reservations', component: ReservationComponent },
      { path: 'announcements', component: AnnoucementComponent },
      { path: 'new-announce', component: AddAnnounceComponent },
      { path: 'edit-profile/:username', component: EditProfileComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}

// { path: 'announcements', component: AnnouncementsComponent },
// { path: 'profile', component: ProfileComponent },
// { path: 'settings', component: SettingsComponent },
