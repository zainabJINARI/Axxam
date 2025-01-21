import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
<<<<<<< Updated upstream
import { HomeComponent } from './client/home/home.component';
import { ClientComponent } from './client/client.component';
=======
import { NotFoundComponent } from './not-found/not-found.component';
import { LoadingComponent } from './loading/loading.component';
>>>>>>> Stashed changes

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
<<<<<<< Updated upstream
  { path: '', redirectTo: 'client', pathMatch: 'full' },  // Redirect to /client if the path is empty
=======
  { path: 'loading', component: LoadingComponent },
  { path: '**', component: NotFoundComponent },
>>>>>>> Stashed changes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
