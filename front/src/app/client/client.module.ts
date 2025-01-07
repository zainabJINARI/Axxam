import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { FeaturesSectionComponent } from './home/features-section/features-section.component';



@NgModule({
  declarations: [
    HomeComponent,
    FeaturesSectionComponent
  ],
  imports: [
    CommonModule
  ] ,
  exports:[FeaturesSectionComponent]
})
export class ClientModule { }
