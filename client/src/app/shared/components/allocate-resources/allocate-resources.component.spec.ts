import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllocateResourcesComponent } from './allocate-resources.component';

describe('AllocateResourcesComponent', () => {
  let component: AllocateResourcesComponent;
  let fixture: ComponentFixture<AllocateResourcesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllocateResourcesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllocateResourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
