import { LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { ManagerServiceService } from 'src/app/core/services/manager-service.service';
import { MentorServiceService } from 'src/app/core/services/mentor-service.service';
import { TraineeServiceService } from 'src/app/core/services/trainee-service.service';
import { TrainerServiceService } from 'src/app/core/services/trainer-service.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  user:any;
  user1:any;
  modalOpen : boolean = false;
  updateMode: boolean = false;
  submitted: boolean = false;

  @Input() title: string;
  @Input() userrole: number;
  @Input() userId: number;

  userForm = this.fb.group({
    firstName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$'),
      Validators.maxLength(50),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$'),
      Validators.maxLength(20),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'),
    ]),
    employeeId: new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]*$'),
      Validators.maxLength(10)
    ]),
    department: new FormControl('', [
      Validators.required
    ]),
  })
  constructor(
    public activeModal: NgbActiveModal,
    public managerService: ManagerServiceService,
    public mentorService: MentorServiceService,
    public trainerService: TrainerServiceService,
    public traineeService: TraineeServiceService,
    public toastrService: ToastrService,
    private fb: FormBuilder,
    private location: LocationStrategy
    )
    { }

  ngOnInit(): void {
    this.getCurrentUser();
    if(this.updateMode){
    switch (this.userrole) {
      case 1:
        this.viewUser(this.managerService,this.userId);
        break;
      case 2:
        this.viewUser(this.mentorService,this.userId);
        break;
      case 3:
        this.viewUser(this.trainerService,this.userId);
        break;
      case 4:
        this.viewUser(this.traineeService,this.userId);
        break;
      default:
        this.toastrService.error('Invalid user role');
        break;
    }
  }

    if (this.activeModal) {
      history.pushState(null, null, window.location.href);
      this.location.onPopState(() => {
        window.onpopstate = function (e) {
          window.history.forward();
        };
        this.activeModal.dismiss();
      });
      window.onpopstate = function (e) {
        window.history.back();
      };
    }
  }

  onInputFocus(event: any) {
    event.target.classList.add('selected-input');
  }

  onInputBlur(event: any) {
    event.target.classList.remove('selected-input');
  }

  getCurrentUser() {
  this.managerService.fetchUser().subscribe({
    next: (resp: any) => {
      this.user1 = resp;
    },
    error: (err: any) => {
      if (err.error.errorCode == "1912") {
        this.toastrService.error('User not found', '');
      } else if (err.error.errorCode == "1908") {
        this.toastrService.error('Invalid token', '');
      } else if (err.error.errorCode == '1909') {
        this.toastrService.error('Authorization token expired', '');
      }
    },
  });
}

  setDefault() {
    this.userForm.patchValue({
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      employeeId: this.user.employeeId,
      department: this.user.department
    });
  }

  validateForm() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.submitted = false;
      return false;
    }
    return true;
  }

  errorMessages = {
    "1912": "User not Found",
    "1093": "User has no permission to access this action",
    "1920": "First Name is required",
    "1921": "First Name size must be between 1 and 50",
    "1923": "Invalid first name",
    "1924": "Last Name is required",
    "1925": "Last Name size must be between 1 and 20",
    "1926": "Invalid last name",
    "1914": "Email is required",
    "1915": "Invalid email format",
    "1930": "Email already exists",
    "1931": "Employee ID is required",
    "1932": "Employee ID already exists",
    "1933": "Department is required",
    "1934": "Invalid department",
    "1913": "Mail sending Failed",
    "1948": "Employee ID must be greater than or equal to 1000",
    "1908": "Invalid Token",
    "1909": "Authorization token expired",
  };

  create() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.submitted = false;
    } else {
    let param = {
      department: this.userForm.value.department,
      email: this.userForm.value.email,
      employeeId: this.userForm.value.employeeId,
      firstName: this.userForm.value.firstName,
      lastName: this.userForm.value.lastName,
    };
    this.submitted = true;
    switch (this.userrole) {
      case 1:
        this.register(this.managerService,param);
        break;
      case 2:
        this.register(this.mentorService,param);
        break;
      case 3:
        this.register(this.trainerService,param);
        break;
      case 4:
        this.register(this.traineeService,param);
        break;
      default:
        this.toastrService.error('Invalid user role');
        break;
    }
  }
  }

  register(service: any,param:any) {
    service.register(param).subscribe(
      (resp: any) => this.onRegisterSuccess(),
      (err: any) => this.onRegisterError(err)
    );
  }

  onRegisterSuccess() {
    this.toastrService.success('Registered Successfully');
    this.activeModal.close();
  }

  onRegisterError(err: any) {
    this.errorFn(err);
  }

  errorFn(err: any){
    let errorMessage;
        this.submitted = false;
        let dataType = typeof err.error.errorCode;
      if(dataType=="undefined"){
        errorMessage=
        this.errorMessages[err.error[0].errorCode];
      }
      else{
        errorMessage=
        this.errorMessages[err.error.errorCode];
      }
      this.toastrService.error(errorMessage, "");
  }

  viewUser(service: any, _userId:any) {
    service.view(this.userId).subscribe({
      next: (resp: any) => {
        this.user=resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == "1912") {
          this.toastrService.error('user not found','');
        } else if (err.error.errorCode == "1093") {
          this.toastrService.error('User has no permission to access this action', '');
        }
        else if (err.error.errorCode == "1908") {
          this.toastrService.error('Invalid token', '');
        }else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
      },
    });
  }

  edit(){
    this.submitted = true;
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.submitted = false;
    } else {
    let param = {
        firstName:this.userForm.value.firstName,
        lastName:this.userForm.value.lastName,
        email:this.userForm.value.email,
        employeeId:this.userForm.value.employeeId,
        department:this.userForm.value.department
      }
      this.submitted=true;
      switch (this.userrole) {
        case 1:
          this.update(this.managerService,param,this.userId);
          break;
        case 2:
          this.update(this.mentorService,param,this.userId);
          break;
        case 3:
          this.update(this.trainerService,param,this.userId);
          break;
        case 4:
          this.update(this.traineeService,param,this.userId);
          break;
        default:
          this.toastrService.error('Invalid user role');
          break;
      }
    }
  }
  update(service: any,param:any,id:any) {
    service.update(param,id).subscribe(
      (resp: any) => this.onUpdateSuccess(),
      (err: any) => this.onUpdateError(err)
    );
  }

  onUpdateSuccess() {
    this.toastrService.success('Updated Successfully');
    this.activeModal.close();
  }

  onUpdateError(err: any) {
    this.errorFn(err);
  }
}
