import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllocateResourceComponent } from './allocate-resource.component';

describe('AllocateResourceComponent', () => {
  let component: AllocateResourceComponent;
  let fixture: ComponentFixture<AllocateResourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllocateResourceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllocateResourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
