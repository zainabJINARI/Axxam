import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExploreSectionComponent } from './explore-section.component';

describe('ExploreSectionComponent', () => {
  let component: ExploreSectionComponent;
  let fixture: ComponentFixture<ExploreSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExploreSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExploreSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
