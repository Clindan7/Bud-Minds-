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
  selector: 'app-forgotpassword',
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css'],
})
export class ForgotpasswordComponent implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserServiceService,
    private fb: FormBuilder,
    private toastrService: ToastrService
  ) {}

  submitted: boolean = false;

  forgotPasswordForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'),
    ]),
  });

  ngOnInit(): void { /* 'ngOnInit' is empty */ }

  forgotPassword() {
    this.submitted = true;
    let param = {
      email: this.forgotPasswordForm.value.email,
    };

    this.userService.forgotPassword(param).subscribe({
      next: (resp: any) => {
        this.toastrService.success('Reset link send to your Email');
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        this.submitted = false;
        if (err.error.errorCode == "1912") {
          this.toastrService.error('User not found');
        } else if (err.error.errorCode == "1914") {
          this.toastrService.error('Email is required');
        } else if (err.error.errorCode == "1915") {
          this.toastrService.error('Invaid email format');
        } else if (err.error.errorCode == "1908") {
          this.toastrService.error('Invaid token');
        }
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }
}
