import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginServiceService } from '../../services/login-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(
    private router: Router,
    private loginService: LoginServiceService,
    private toastrService: ToastrService  ) {}

  ngOnInit(): void { /* 'ngOnInit' is empty */ }

  userLoginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$'
      ),
    ]),
  });
  public resetPasswordForm: FormGroup | any;
  submitted: boolean = false;
  public showPassword: boolean = false;
  public showPasswordOnPress: boolean = false;
  passwordMatch: any;
  public is_visible: boolean = false;
  reset: any;
  newFlag: any;
  confirmFlag: any;
  status:any;

  errorMessages = {
    "1910": "Invalid credentials",
    "1922": "Password is wrong",
    "1912": "User not found",
    "1914": "Email is required",
    "1915": "Invalid email format",
    "1916": "Password is required",
    "1908": "Invalid Token",
    "1909": "Authorization token expired",
  };

  userLogin(): void {
    this.submitted = true;
    if (this.userLoginForm.invalid) {
      this.userLoginForm.markAllAsTouched();
      return;
    }

    const obj = {
      email: this.userLoginForm.value.email,
      password: this.userLoginForm.value.password,
    };

    this.loginService.userLogin(obj).subscribe({
      next: (res: any) => {
        this.handleLoginSuccess(res.status);
      },
      error: (err: any) => {
        this.submitted = false;
        const errorMessage =
        this.errorMessages[err.error.errorCode];
      this.toastrService.error(errorMessage, "");
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }

  private handleLoginSuccess(status: number): void {
    if (status === 2) {
      this.router.navigateByUrl('change_password');
    } else {
      this.router.navigateByUrl('dashboard');
    }
  }
}
