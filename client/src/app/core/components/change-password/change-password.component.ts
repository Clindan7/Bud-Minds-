import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from '../../services/user-service.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  constructor(private router: Router,
    private route: ActivatedRoute,
    private userService: UserServiceService,
    private fb: FormBuilder,
    private toastrService: ToastrService
) {
  this.changePasswordForm = this.fb.group({

    currentPassword: new FormControl('', [
     Validators.required,
     Validators.minLength(8),
     Validators.maxLength(20),
     Validators.pattern(
      '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
    ),
  ]),
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


 public changePasswordForm: FormGroup | any;
 submitted: boolean = false;
 public showCurrentpassword: boolean = false;
 public showPassword: boolean = false;
 public showPasswordOnPress: boolean = false;
 passwordMatch: any;
 public is_visible: boolean = false;
 reset: any;


  ngOnInit(): void {
    // 'ngOnInit' is empty
  }

  validateForm() {
    if (this.changePasswordForm.invalid) {
      this.changePasswordForm.markAllAsTouched();
      this.submitted = false;
      return false;
    }
    return true;
  }

  errorMessages = {
    "1911": "Entered password same as previous password",
    "1916": "Password is required",
    "1917": "Password should at least contain one special character,lowercase,uppercase and digit",
    "1918": "Size must be between 8 and 20",
    "1912": "User not found",
    "1929": "Current Password is required",
    "1928": "Current password is not correct",
    "1908": "Invalid Token",
    "1909": "Authorization token expired",
  };

  changePassword() {
    this.submitted = true;

    if (!this.validateForm()) {
      return;
    }

    if (this.changePasswordForm.value.newPassword !== this.changePasswordForm.value.confirmPassword) {
      this.toastrService.error('Passwords are not same', '');
      this.submitted=false;
    }

    else{
    const param = {
      currentPassword: this.changePasswordForm.value.currentPassword,
      newPassword: this.changePasswordForm.value.confirmPassword
    };

    this.userService.changePassword(param).subscribe({
      next: (resp: any) => {
        this.toastrService.success('Password successfully changed', '');
        this.logout();
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

  logout()
  {
    localStorage.removeItem("accessToken")
    this.router.navigateByUrl("/login")

  }

  }

