import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';
import { LoginServiceService } from '../../services/login-service.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let service: LoginServiceService;
  let toastrService: jasmine.SpyObj<ToastrService>;
  let spy: jasmine.Spy;

  beforeEach(async () => {
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule,
        RouterTestingModule.withRoutes([
          { path: '', component: LoginComponent }
        ]) ,
        HttpClientTestingModule,
        ToastrModule.forRoot()
      ],
      providers: [LoginServiceService,{ provide: ToastrService, useValue: toastrService }],
      declarations: [ LoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(LoginServiceService);
    fixture.detectChanges();
  });

  it('should create Login Component', () => {
    expect(component).toBeTruthy();
  });

  it('should create a form with email and password controls', () => {
    expect(component.userLoginForm.contains('email')).toBeTruthy();
    expect(component.userLoginForm.contains('password')).toBeTruthy();
  });

  it('should render email input field', () => {
    const emailInput = fixture.debugElement.query(By.css('#email'));
    expect(emailInput).toBeTruthy();
  });

  it('should have the correct placeholder text for email input field', () => {
    const emailInput = fixture.debugElement.query(By.css('#email'));
    expect(emailInput.nativeElement.getAttribute('placeholder')).toEqual('Enter email');
  });

  it('should mark the email control as invalid when empty', () => {
    const emailControl = component.userLoginForm.get('email');
    emailControl.setValue('');
    expect(emailControl.valid).toBeFalsy();
  });

  it('should mark the email control as invalid when an invalid email is entered', () => {
    const emailControl = component.userLoginForm.get('email');
    emailControl.setValue('invalid-email');
    expect(emailControl.valid).toBeFalsy();
  });

  it('should mark the email control as valid when a valid email is entered', () => {
    const emailControl = component.userLoginForm.get('email');
    emailControl.setValue('valid-email@example.com');
    expect(emailControl.valid).toBeTruthy();
  });

  it('should mark the password control as invalid when empty', () => {
    const passwordControl = component.userLoginForm.get('password');
    passwordControl.setValue('');
    expect(passwordControl.valid).toBeFalsy();
  });

  it('should mark the password control as invalid when an invalid password is entered', () => {
    const passwordControl = component.userLoginForm.get('password');
    passwordControl.setValue('invalid');
    expect(passwordControl.valid).toBeFalsy();
  });

  it('should mark the password control as valid when a valid password is entered', () => {
    const passwordControl = component.userLoginForm.get('password');
    passwordControl.setValue('ValidPassword123@');
    expect(passwordControl.valid).toBeTruthy();
  });

  it('should submit the form when valid email and password are entered', () => {
    spyOn(component, 'userLogin');
    const emailControl = component.userLoginForm.get('email');
    emailControl.setValue('valid-email@example.com');
    const passwordControl = component.userLoginForm.get('password');
    passwordControl.setValue('ValidPassword123@');
    const submitButton = fixture.debugElement.query(By.css('button[type=submit]')).nativeElement;
    submitButton.click();
    fixture.detectChanges();
    expect(component.userLogin).toHaveBeenCalled();
  });

  it(' login service should be created', () => {
    expect(service).toBeTruthy();
  });

});
