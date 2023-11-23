import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraineeSessionListComponent } from './trainee-session-list.component';

describe('TraineeSessionListComponent', () => {
  let component: TraineeSessionListComponent;
  let fixture: ComponentFixture<TraineeSessionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraineeSessionListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraineeSessionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
