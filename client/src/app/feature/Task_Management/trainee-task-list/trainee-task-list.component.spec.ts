import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraineeTaskListComponent } from './trainee-task-list.component';

describe('TraineeTaskListComponent', () => {
  let component: TraineeTaskListComponent;
  let fixture: ComponentFixture<TraineeTaskListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraineeTaskListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraineeTaskListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
