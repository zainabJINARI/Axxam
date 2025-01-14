import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './client/home/home.component';
import { AdminComponent } from './admin/admin.component';

import { AnnouncementDetailComponent } from './client/announcement-detail/announcement-detail.component';
import { LoginComponent } from './login/login.component';
import { SearchPageComponent } from './client/search-page/search-page.component';
import { SignupComponent } from './signup/signup.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'details', component: AnnouncementDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'search', component: SearchPageComponent },
  { path: 'signup', component: SignupComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
