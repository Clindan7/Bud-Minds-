import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { UserServiceService } from '../../services/user-service.service';

import { ForgotpasswordComponent } from './forgotpassword.component';

describe('ForgotpasswordComponent', () => {
  let component: ForgotpasswordComponent;
  let fixture: ComponentFixture<ForgotpasswordComponent>;
  let service: UserServiceService;
  let toastrService: jasmine.SpyObj<ToastrService>;
  let spy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule,
        RouterTestingModule ,
        HttpClientTestingModule,
        ToastrModule.forRoot()
      ],
      declarations: [ ForgotpasswordComponent ],
      providers: [
        UserServiceService,
        { provide: ToastrService, useValue: toastrService },
        { provide: ActivatedRoute, useValue: {} },
        FormBuilder
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotpasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
