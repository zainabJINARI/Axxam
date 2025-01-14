import { NgModule } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { HomeComponent } from './home/home.component';

import { FeaturesSectionComponent } from './home/features-section/features-section.component';
import { HeaderComponent } from './sub-components/header/header.component';
import { HeroSectionComponent } from './sub-components/hero-section/hero-section.component';
import { SearchComponent } from './sub-components/search/search.component';
import { ExploreSectionComponent } from './sub-components/explore-section/explore-section.component';
import { CardAnnouncementComponent } from './sub-components/card-announcement/card-announcement.component';
import { FilterSubComponent } from './sub-components/filter-sub/filter-sub.component';
import { CategoryComponent } from './sub-components/category/category.component';
import { AnnouncementDetailComponent } from './announcement-detail/announcement-detail.component';
import { GridImgsComponent } from './sub-components/grid-imgs/grid-imgs.component';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
import { GenenralInfoAnnComponent } from './sub-components/genenral-info-ann/genenral-info-ann.component';
import { ReservationFormComponent } from './sub-components/reservation-form/reservation-form.component';
import { ReviewComponent } from './sub-components/review/review.component';
import { AddReviewComponent } from './sub-components/add-review/add-review.component';

import { MatIconModule } from '@angular/material/icon';
import { SearchPageComponent } from './search-page/search-page.component';


@NgModule({
  declarations: [
    HomeComponent,
    HeroSectionComponent,
    SearchComponent,
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






    

  ],
  imports: [
    CommonModule,
    MatGridList,
    MatGridTile,
    MatIconModule,
    NgFor,
    NgIf,
  ]

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
