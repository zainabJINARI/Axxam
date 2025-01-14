import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GridImgsComponent } from './grid-imgs.component';

describe('GridImgsComponent', () => {
  let component: GridImgsComponent;
  let fixture: ComponentFixture<GridImgsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GridImgsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GridImgsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
