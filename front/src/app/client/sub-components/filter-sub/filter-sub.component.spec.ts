import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterSubComponent } from './filter-sub.component';

describe('FilterSubComponent', () => {
  let component: FilterSubComponent;
  let fixture: ComponentFixture<FilterSubComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilterSubComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilterSubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
