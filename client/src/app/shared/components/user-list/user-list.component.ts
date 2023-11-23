import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { ManagerServiceService } from 'src/app/core/services/manager-service.service';
import { MentorServiceService } from 'src/app/core/services/mentor-service.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { TraineeServiceService } from 'src/app/core/services/trainee-service.service';
import { TrainerServiceService } from 'src/app/core/services/trainer-service.service';
import { ManagerDelConfirmationComponent } from '../../modal/manager-del-confirmation/manager-del-confirmation.component';
import { ConfirmationModelComponent } from '../confirmation-model/confirmation-model.component';
import { UserFormComponent } from '../user-form/user-form.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  status:any;
  user:any;
  user1: any;
  page:any=1;
  limit:any=10
  len:any
  modalRef:any;
  modalReff:any;
  keyword = 'firstName';
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;

  @Input() userrole: any;
  @Input() userData: any;

  @Output() userPage: EventEmitter<any> = new EventEmitter<any>();

  searchFirstName:FormGroup = new FormGroup({
    name: new FormControl(""),
    employeeId : new FormControl(""),
    status : new FormControl("")
  })

  constructor(
    private managerService: ManagerServiceService,
    public mentorService: MentorServiceService,
    public trainerService: TrainerServiceService,
    public traineeService: TraineeServiceService,
    private toast: ToastrService,
    private modalService:NgbModal,
    private router: Router,
    private skeletonLoaderService: SkeletonLoaderService
    )
  { }

  ngOnInit(): void {

    let userService: any;

    switch (this.userrole) {
      case 1:
        userService = this.managerService;
        break;
      case 2:
        userService = this.mentorService;
        break;
      case 3:
        userService = this.trainerService;
        break;
      case 4:
        userService = this.traineeService;
        break;
      default:
        this.toast.error('Invalid user role');
        return;
    }

    userService.searchUser().subscribe({
      next: (resp: any) => {
        if (resp) {
          this.user1 = resp;
        }
      },
      error: (err: any) => {
        if (err.error.errorCode == "1912") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });

    userService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
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

    this.search(this.page,this.limit);
    // this.getCurrentUser();
  }

  clearSearchTerm(): void {
    this.searchFirstName.controls['employeeId'].reset();
    this.searchFirstName.value.employeeId="";
  }

  omit_special_char(event)
  {
    let k;
    k = event.charCode;
    return((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8 || (k >= 48 && k <= 57));
  }

  search(page: any, limit: any, searchMgr?: any) {
    if((this.searchFirstName.value.employeeId!="")|| this.searchFirstName.value.name!="" || this.searchFirstName.value.status!=""){
      this.page=1
    }

    let userService: any;

    switch (this.userrole) {
      case 1:
        userService = this.managerService;
        break;
      case 2:
        userService = this.mentorService;
        break;
      case 3:
        userService = this.trainerService;
        break;
      case 4:
        userService = this.traineeService;
        break;
      default:
        this.toast.error('Invalid user role');
        return;
    }

    userService.listUsers(page, limit,this.searchFirstName.value).subscribe({
      next: (res: any) => {
        this.len = res.numItems;
        this.userData = res;
        this.toggleSkeleton(false);
      },
      error: (err: any) => {
        if (err.error.errorCode == "1950") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1912") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1931") {
          this.toast.error('Employee ID should be a positive integer', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
        this.userData = [];
      }
    });
  }

  toggleSkeleton(flag: boolean): void {
    this.skeletonFlag = flag;
    this.skeletonLoaderService.setLoading(flag);
  }

register() {
  let modalRef = this.modalService.open(UserFormComponent, {
    centered: true,
    backdrop:'static'
  });
  if(this.userrole==1){
    modalRef.componentInstance.title = 'Register Manager';
    modalRef.componentInstance.userrole = 1;
    }
    else if(this.userrole==2){
      modalRef.componentInstance.title = 'Register Mentor';
      modalRef.componentInstance.userrole = 2;
    }
    else if(this.userrole==3){
      modalRef.componentInstance.title = 'Register Trainer';
      modalRef.componentInstance.userrole = 3;
    }
    else if(this.userrole==4){
      modalRef.componentInstance.title = 'Register Trainee';
      modalRef.componentInstance.userrole = 4;
    }
  modalRef.result.then(
    (result) => {
      this.search(this.page,this.limit);
    },
    () => { });
}

update(userId:any) {
  let modalRef = this.modalService.open(UserFormComponent, {
    centered: true,
    backdrop:'static'
  });
  if(this.userrole==1){
  modalRef.componentInstance.title = 'Edit Manager';
  modalRef.componentInstance.userrole = 1;
  }
  else if(this.userrole==2){
    modalRef.componentInstance.title = 'Edit Mentor';
    modalRef.componentInstance.userrole = 2;
  }
  else if(this.userrole==3){
    modalRef.componentInstance.title = 'Edit Trainer';
    modalRef.componentInstance.userrole = 3;
  }
  else if(this.userrole==4){
    modalRef.componentInstance.title = 'Edit Trainee';
    modalRef.componentInstance.userrole = 4;
  }
  modalRef.componentInstance.userId = userId;
  modalRef.componentInstance.updateMode = true;
  modalRef.result.then(
    (result) => {
      this.search(this.page,this.limit);
    },
    () => { });
}

delete(userId:any) {
  let userService: any;
  {
    this.modalRef = this.modalService.open(ConfirmationModelComponent, {
    centered: true,
    backdrop:'static'
  });
  if(this.userrole==1){
  this.modalRef.componentInstance.message = 'Are you sure you want to delete this manager?';
  }
  else if(this.userrole==2){
  this.modalRef.componentInstance.message = 'Are you sure you want to delete this mentor?';
  }
  else if(this.userrole==3){
    this.modalRef.componentInstance.message = 'Are you sure you want to delete this trainer?';
  }
  else if(this.userrole==4){
  this.modalRef.componentInstance.message = 'Are you sure you want to delete this trainee?';
  }
  this.modalRef.componentInstance.title = 'Confirm Delete';
  this.modalRef.result.then((result) => {
    if (result === 'confirm') {


    switch (this.userrole) {
      case 1:
        userService = this.managerService;
        break;
      case 2:
        userService = this.mentorService;
        break;
      case 3:
        userService = this.trainerService;
        break;
      case 4:
        userService = this.traineeService;
        break;
      default:
        this.toast.error('Invalid user role');
        return;
    }
      userService.delete(userId).subscribe({
      next: (res:any) => {
        this.toast.success('Deleted successfully');

      },
      error: (err:any) => {

        if (err.error.errorCode == "1912") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1093"){
          this.toast.error('User has no permission to access this action', '');
        } else if (err.error.errorCode == '1908') {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        } else if (err.error.errorCode == "1943"){
          this.modalReff = this.modalService.open(ManagerDelConfirmationComponent, {
            centered: true,
          });
          this.modalReff.componentInstance.message = 'Cannot delete manager, please deallocate the resources';
          this.modalReff.componentInstance.title = 'Please deallocate';
          this.modalReff.componentInstance.managerId=userId;

                }
      },
      complete: () => {
        this.search(this.page, this.limit);
      },
    })
    }
  }).catch((error) => {
  });
}

}

onPageChange($event) {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });

  this.page = $event;
  this.search(this.page, this.limit);
}
}
