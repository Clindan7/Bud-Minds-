import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';

import { GroupAllocationComponent } from './group-allocation.component';

describe('GroupAllocationComponent', () => {
  let component: GroupAllocationComponent;
  let fixture: ComponentFixture<GroupAllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GroupAllocationComponent],
      imports: [NgbNavModule]
    }).compileComponents();

    fixture = TestBed.createComponent(GroupAllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
