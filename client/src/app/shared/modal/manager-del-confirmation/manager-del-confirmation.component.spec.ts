import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ManagerDelConfirmationComponent } from './manager-del-confirmation.component';

describe('ManagerDelConfirmationComponent', () => {
  let component: ManagerDelConfirmationComponent;
  let fixture: ComponentFixture<ManagerDelConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NgbModule
      ],
      providers: [
        NgbActiveModal
        ],
      declarations: [ ManagerDelConfirmationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerDelConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
