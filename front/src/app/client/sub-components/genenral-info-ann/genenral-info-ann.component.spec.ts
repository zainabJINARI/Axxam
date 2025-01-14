import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenenralInfoAnnComponent } from './genenral-info-ann.component';

describe('GenenralInfoAnnComponent', () => {
  let component: GenenralInfoAnnComponent;
  let fixture: ComponentFixture<GenenralInfoAnnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GenenralInfoAnnComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenenralInfoAnnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
