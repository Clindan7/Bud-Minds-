import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MentorDelConfirmationComponent } from './mentor-del-confirmation.component';

describe('MentorDelConfirmationComponent', () => {
  let component: MentorDelConfirmationComponent;
  let fixture: ComponentFixture<MentorDelConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NgbModule
      ],
      providers: [
        NgbActiveModal
        ],
      declarations: [ MentorDelConfirmationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MentorDelConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
