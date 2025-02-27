import { NgModule } from '@angular/core';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { HomeComponent } from './home/home.component';
import { HeroSectionComponent } from './sub-components/hero-section/hero-section.component';
import { SearchComponent } from './sub-components/search/search.component';
import { FeaturesSectionComponent } from './sub-components/features-section/features-section.component';
import { FooterComponent } from './sub-components/footer/footer.component';
import { PropertyListingComponent } from './sub-components/property-listing/property-listing.component';
import { ExploreSectionComponent } from './sub-components/explore-section/explore-section.component';
import { CardAnnouncementComponent } from './sub-components/card-announcement/card-announcement.component';
import { FilterSubComponent } from './sub-components/filter-sub/filter-sub.component';
import { CategoryComponent } from './sub-components/category/category.component';
import { AnnouncementDetailComponent } from './announcement-detail/announcement-detail.component';
import { GridImgsComponent } from './sub-components/grid-imgs/grid-imgs.component';
import { GenenralInfoAnnComponent } from './sub-components/genenral-info-ann/genenral-info-ann.component';
import { ReservationFormComponent } from './sub-components/reservation-form/reservation-form.component';
import { ReviewComponent } from './sub-components/review/review.component';
import { AddReviewComponent } from './sub-components/add-review/add-review.component';
import { SearchPageComponent } from './search-page/search-page.component';
import { ClientComponent } from './client.component';
import { ClientRoutingModule } from './client-routing.module';
import { HeaderComponent } from './sub-components/header/header.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { PopupComponent } from '../popup/popup.component';
import { SharedModule } from '../shared/shared/shared.module';

@NgModule({
  declarations: [
    HomeComponent,
    FeaturesSectionComponent,
    HeroSectionComponent,
    SearchComponent,
    FooterComponent,
    PropertyListingComponent,
    ExploreSectionComponent,
    CardAnnouncementComponent,
    FilterSubComponent,
    CategoryComponent,
    AnnouncementDetailComponent,
    GridImgsComponent,
    GenenralInfoAnnComponent,
    ReservationFormComponent,
    ReviewComponent,
    AddReviewComponent,
    SearchPageComponent,
    ClientComponent,
    HeaderComponent,

  ],
  imports: [
    CommonModule,
    MatGridListModule,
    MatIconModule,
    ClientRoutingModule,
    RouterModule,
    NgFor,
    NgIf,
    ReactiveFormsModule,
    SharedModule
    
],
  exports: [
    FeaturesSectionComponent,
    HeroSectionComponent,
    PropertyListingComponent, // Exporte les composants nécessaires pour l'utilisation dans d'autres modules
  ],
  providers: [DatePipe],
})
export class ClientModule {}
