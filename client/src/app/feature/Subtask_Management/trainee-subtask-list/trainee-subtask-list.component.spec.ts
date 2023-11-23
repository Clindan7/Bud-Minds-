import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraineeSubtaskListComponent } from './trainee-subtask-list.component';

describe('TraineeSubtaskListComponent', () => {
  let component: TraineeSubtaskListComponent;
  let fixture: ComponentFixture<TraineeSubtaskListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraineeSubtaskListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraineeSubtaskListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
