import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubtaskManagementComponent } from './subtask-management.component';

describe('SubtaskManagementComponent', () => {
  let component: SubtaskManagementComponent;
  let fixture: ComponentFixture<SubtaskManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubtaskManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubtaskManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
