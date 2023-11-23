import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';

import { ManagerResourceAllocationComponent } from './manager-resource-allocation.component';

describe('ManagerResourceAllocationComponent', () => {
  let component: ManagerResourceAllocationComponent;
  let fixture: ComponentFixture<ManagerResourceAllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagerResourceAllocationComponent ],
      imports: [NgbNavModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerResourceAllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
