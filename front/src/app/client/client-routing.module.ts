import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';
import { HomeComponent } from './home/home.component';
import { AnnouncementDetailComponent } from './announcement-detail/announcement-detail.component';
import { SearchPageComponent } from './search-page/search-page.component';
<<<<<<< Updated upstream
import { AuthGuard } from '../guards/auth.guard';
import { NotFoundComponent } from '../not-found/not-found.component';
=======
import { NotFoundComponent } from '../not-found/not-found.component';

>>>>>>> Stashed changes

const routes: Routes = [
  {
    path: 'client',
    component: ClientComponent,
    children: [
      { path: '', component: HomeComponent }, // No guard for this route
      {
        path: 'announcement/:id',
        component: AnnouncementDetailComponent,
        canActivate: [AuthGuard],
      }, // Guard applied
      {
        path: 'explore',
        component: SearchPageComponent,
        canActivate: [AuthGuard],
      },
      { path: '**', component: NotFoundComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ClientRoutingModule {}
