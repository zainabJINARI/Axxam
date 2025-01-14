import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ClientModule } from './client/client.module';
import { HeaderComponent } from './client/sub-components/header/header.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { LoginComponent } from './login/login.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { SignupComponent } from './signup/signup.component';

@NgModule({
  declarations: [
    AppComponent , 
    ],
    AppComponent,
  
    HeaderComponent,
        LoginComponent,
        SignupComponent,
      
       
  ],
  imports: [
    BrowserModule,
    AppRoutingModule ,
    MatGridListModule, // Add it here
    ClientModule,

  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
