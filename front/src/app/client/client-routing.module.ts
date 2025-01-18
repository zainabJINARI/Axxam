import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';
import { HomeComponent } from './home/home.component';
import { AnnouncementDetailComponent } from './announcement-detail/announcement-detail.component';
import { SearchPageComponent } from './search-page/search-page.component';


const routes: Routes = [
  {
    path: 'client',
    component: ClientComponent,
    children: [
      { path: '', component: HomeComponent },
      { path: 'announcement/:id', component: AnnouncementDetailComponent },
      { path: 'search', component: SearchPageComponent },
    ],
  },
];

@NgModule({
  
  imports: [
    RouterModule.forChild(routes)
  ],
  exports:[RouterModule]
})
export class ClientRoutingModule { }
