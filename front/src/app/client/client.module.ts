import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './sub-components/header/header.component';
import { HeroSectionComponent } from './sub-components/hero-section/hero-section.component';
import { SearchComponent } from './sub-components/search/search.component';
import { FeaturesSectionComponent } from './sub-components/features-section/features-section.component';
import { FooterComponent } from './sub-components/footer/footer.component';
import { PropertyListingComponent } from './sub-components/property-listing/property-listing.component';

@NgModule({
  declarations: [
    HomeComponent,
    FeaturesSectionComponent,
    HeaderComponent,
    HeroSectionComponent,
    SearchComponent,
    FooterComponent,
    PropertyListingComponent,
  ],
  imports: [CommonModule],
  exports: [FeaturesSectionComponent],
})
export class ClientModule {}
