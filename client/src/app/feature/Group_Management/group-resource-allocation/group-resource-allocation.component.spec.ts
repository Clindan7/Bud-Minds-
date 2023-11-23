import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupResourceAllocationComponent } from './group-resource-allocation.component';

describe('GroupResourceAllocationComponent', () => {
  let component: GroupResourceAllocationComponent;
  let fixture: ComponentFixture<GroupResourceAllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupResourceAllocationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupResourceAllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
