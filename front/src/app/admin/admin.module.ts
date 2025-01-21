import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './admin-components/sidebar/sidebar.component';
import { AdminComponent } from './admin.component';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './admin-components/navbar/navbar.component';
import { DashboardComponent } from './admin-components/dashboard/dashboard.component';
import { ReservationComponent } from './admin-components/reservation/reservation.component';
import { AdminRoutingModule } from './admin-routing.module';
import { AnnoucementComponent } from './admin-components/annoucement/annoucement.component';
import { AddAnnounceComponent } from './admin-components/add-announce/add-announce.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EditProfileComponent } from './admin-components/edit-profile/edit-profile.component';
import { EditAnnouncementComponent } from './admin-components/edit-announcement/edit-announcement.component';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { NotificationsComponent } from './admin-components/notifications/notifications.component';
@NgModule({
  declarations: [
    SidebarComponent,
    AdminComponent,
    NavbarComponent,
    DashboardComponent,
    ReservationComponent,
    AnnoucementComponent,
    AddAnnounceComponent,
    EditProfileComponent,
    EditAnnouncementComponent,
    NotificationsComponent,
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ReactiveFormsModule,
    RouterModule,
    FormsModule,
    CanvasJSAngularChartsModule,
  ],
  exports: [SidebarComponent],
})
export class AdminModule {}
