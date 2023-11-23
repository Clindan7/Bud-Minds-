import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinerGroupComponent } from './joiner-group.component';

describe('JoinerGroupComponent', () => {
  let component: JoinerGroupComponent;
  let fixture: ComponentFixture<JoinerGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JoinerGroupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JoinerGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
