import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ConfirmationModelComponent } from './confirmation-model.component';

describe('ConfirmationModelComponent', () => {
  let component: ConfirmationModelComponent;
  let fixture: ComponentFixture<ConfirmationModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NgbModule
      ],
      declarations: [ ConfirmationModelComponent ],
      providers: [
        NgbActiveModal
        ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmationModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
