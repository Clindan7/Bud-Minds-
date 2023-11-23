import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ChangePasswordComponent } from './change-password.component';
import { ToastrService } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserServiceService } from '../../services/user-service.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ChangePasswordComponent', () => {
  let component: ChangePasswordComponent;
  let fixture: ComponentFixture<ChangePasswordComponent>;
  let service: UserServiceService;
  let toastrService: jasmine.SpyObj<ToastrService>;
  let spy: jasmine.Spy;

  beforeEach(async () => {
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [ ChangePasswordComponent ],
      providers: [
        UserServiceService,
        { provide: ToastrService, useValue: toastrService },
        { provide: ActivatedRoute, useValue: {} },
        FormBuilder
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
