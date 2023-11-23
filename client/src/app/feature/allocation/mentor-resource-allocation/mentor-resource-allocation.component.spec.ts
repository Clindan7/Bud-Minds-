import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';

import { MentorResourceAllocationComponent } from './mentor-resource-allocation.component';

describe('MentorResourceAllocationComponent', () => {
  let component: MentorResourceAllocationComponent;
  let fixture: ComponentFixture<MentorResourceAllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MentorResourceAllocationComponent],
      imports: [NgbNavModule]
    }).compileComponents();

    fixture = TestBed.createComponent(MentorResourceAllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
