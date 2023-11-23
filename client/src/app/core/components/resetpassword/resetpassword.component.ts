import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from '../../services/user-service.service';

@Component({
  selector: 'app-resetpassword',
  templateUrl: './resetpassword.component.html',
  styleUrls: ['./resetpassword.component.css'],
})
export class ResetpasswordComponent implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserServiceService,
    private fb: FormBuilder,
    private toastrService: ToastrService
  ) {
    this.resetPasswordForm = this.fb.group({
      newPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(
          '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
        ),
      ]),
      confirmPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(
          '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
        ),
      ]),
    });
  }

  public resetPasswordForm: FormGroup | any;
  submitted: boolean = false;
  public showPassword: boolean = false;
  public showPasswordOnPress: boolean = false;
  public is_visible: boolean = false;
  reset: any;

  ngOnInit(): void {
    this.reset = this.route.params["_value"].token;
    this.validateToken();
  }

  errorMessages = {
    "1908": "Password link is invalid",
    "1909": "Password link expired",
    "1911": "Password is Old Please enter a new password",
    "1916": "Please enter new password",
    "1917": "Password should at least contain one special character,lowercase,uppercase and digit",
    "1918": "Size must be between 8 and 20",
    "1919": "Email Token is required",
    "1930": "Invalid Token",
  };

  resetPassword() {
    if (this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      this.submitted = false;
    }

    else if (this.resetPasswordForm.value.newPassword !== this.resetPasswordForm.value.confirmPassword) {
      this.toastrService.error('Passwords are not same', '');
    }

    else{
    const params = {
      newPassword: this.resetPasswordForm.value.confirmPassword,
      emailToken: this.reset,
    };

    this.userService.resetPassword(params).subscribe({
      next: (resp: any) => {
        this.toastrService.success('Password successfully changed', '');
        this.router.navigate(['/login']);
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
  }

  validateToken(){
    this.userService.validateToken(this.reset).subscribe({
      next: (resp:any)=>{
      },
      error:(err : any)=>{
        if (err.error.errorCode == "1908") {
          this.toastrService.error('Password link is invalid', '');
        } else if (err.error.errorCode == "1909") {
          this.toastrService.error('Password link expired', '');
        }
        else if (err.error.errorCode == "1919") {
          this.toastrService.error('Email reset token is required', '');
        }
        this.router.navigate(['/login']);
      }
    })
  }
}
