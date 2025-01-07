import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { FeaturesSectionComponent } from './home/features-section/features-section.component';
import { HeaderComponent } from './sub-components/header/header.component';
import { HeroSectionComponent } from './sub-components/hero-section/hero-section.component';
import { SearchComponent } from './sub-components/search/search.component';

@NgModule({
  declarations: [
    HomeComponent,
    FeaturesSectionComponent,
    HeaderComponent,
    HeroSectionComponent,
    SearchComponent,
  ],
  imports: [CommonModule],
  exports: [FeaturesSectionComponent],
})
export class ClientModule {}
