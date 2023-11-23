import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinerBatchComponent } from './joiner-batch.component';

describe('JoinerBatchComponent', () => {
  let component: JoinerBatchComponent;
  let fixture: ComponentFixture<JoinerBatchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JoinerBatchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JoinerBatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
