import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from 'src/app/core/services/user-service.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  user: any;
  updateMode: boolean = false;
  submitted: boolean = false;
  updateForm = new FormGroup({
    firstName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-z]+([\\s][a-zA-Z]+)*$'),
      Validators.maxLength(50),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-z]+([\\s][a-zA-Z]+)*$'),
      Validators.maxLength(20),
    ]),
  });

  constructor(
    private userService: UserServiceService,
    private fb: FormBuilder,
    private router: Router,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.userService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == 1912) {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  setDefault() {
    this.updateForm.patchValue({
      firstName: this.user.firstName,
      lastName: this.user.lastName,
    });
  }

  updateProfile() {
    if (this.updateForm.valid) {
      let param = {
        firstName: this.updateForm.value.firstName,
        lastName: this.updateForm.value.lastName,
      };
      this.userService.updateProfile(param).subscribe({
        next: (resp: any) => {
          this.toast.success('Profile Updated Successfully', '');
          this.submitted = true;
          this.updateMode = false;
          this.getCurrentUser();
        },
        error: (err: any) => {
          if (err.error.errorCode == "1920") {
            this.toast.error('First Name is required', '');
          } else if (err.error.errorCode == "1921") {
            this.toast.error('First Name size must be between 1 and 50', '');
          } else if (err.error.errorCode == "1923") {
            this.toast.error('Invalid first name', '');
          } else if (err.error.errorCode == "1924") {
            this.toast.error('Last Name is required', '');
          } else if (err.error.errorCode == "1925") {
            this.toast.error('Last Name size must be between 1 and 20', '');
          } else if (err.error.errorCode == "1926") {
            this.toast.error('Invalid last name', '');
          }
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }
}
