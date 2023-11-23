import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraineeValuationComponent } from './trainee-valuation.component';

describe('TraineeValuationComponent', () => {
  let component: TraineeValuationComponent;
  let fixture: ComponentFixture<TraineeValuationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraineeValuationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraineeValuationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
