import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';
import { HomeComponent } from './home/home.component';
import { AnnouncementDetailComponent } from './announcement-detail/announcement-detail.component';
import { SearchComponent } from './sub-components/search/search.component';
import { SearchPageComponent } from './search-page/search-page.component';


const routes: Routes = [
  {
    path: 'client',
    component: ClientComponent,
    children: [
      { path: '', component: HomeComponent },
      { path: 'details', component: AnnouncementDetailComponent },
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
