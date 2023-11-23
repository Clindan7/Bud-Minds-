import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ManagerResourceAllocationService } from 'src/app/core/services/manager-resource-allocation.service';

@Component({
  selector: 'app-trainee-to-manager',
  templateUrl: './trainee-to-manager.component.html',
  styleUrls: ['./trainee-to-manager.component.css'],
})
export class TraineeToManagerComponent implements OnInit {
  constructor(
    private managerResource: ManagerResourceAllocationService,
    private route: ActivatedRoute,
    private toast: ToastrService
  ) {}

  active = 1;
  users: any;
  allocate: any;
  deallocate: any;
  managerId: any;
  newArray: Array<number>[] = [];
  checked: any = 0;
  userRole: any = 4;

  ngOnInit(): void {
    this.managerId = this.route.snapshot.params['id'];
    // this.assignedTrainees();
    // this.totalTrainees();
  }

  // assignedTrainees() {
  //   this.newArray.length = 0;
  //   this.managerResource
  //     .assignedResources(this.managerId, this.userRole)
  //     .subscribe({
  //       next: (resp: any) => {
  //         this.deallocate = resp;
  //       },
  //       error: (err: any) => {
  //         if (err.error.errorCode == '1093') {
  //           this.toast.error(
  //             'User has no permission to access this action',
  //             ''
  //           );
  //         } else if (err.error.errorCode == '1912') {
  //           this.toast.error('User not found', '');
  //         } else if (err.error.errorCode == '1996') {
  //           this.toast.error('Manager not found', '');
  //         } else if (err.error.errorCode == '1995') {
  //           this.toast.error('Invalid user role', '');
  //         } else {
  //           this.toast.error('An unexpected error has occured', '');
  //         }
  //       },
  //     });
  // }

  // totalTrainees() {
  //   this.newArray.length = 0;
  //   this.managerResource.totalResources(4).subscribe({
  //     next: (resp: any) => {
  //       this.allocate = resp;
  //     },
  //     error: (err: any) => {
  //       if (err.error.errorCode == '1093') {
  //         this.toast.error('User has no permission to access this action', '');
  //       } else if (err.error.errorCode == '1912') {
  //         this.toast.error('User not found', '');
  //       } else if (err.error.errorCode == '1996') {
  //         this.toast.error('Manager not found', '');
  //       } else if (err.error.errorCode == '1995') {
  //         this.toast.error('Invalid user role', '');
  //       } else {
  //         this.toast.error('An unexpected error has occured', '');
  //       }
  //     },
  //   });
  // }

  allocateTrainees() {
    let param = {
      users: this.newArray,
    };
    this.managerResource
      .allocateResources(1, this.managerId, this.userRole, param)
      .subscribe({
        next: (resp: any) => {
          this.allocate = resp;
          this.toast.success('Trainees sucessfully allocated to manager', '');
          this.newArray.length = 0;
          // this.totalTrainees();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }else {
            this.toast.error('An unexpected error has occured', '');
          }
        },
      });
  }

  deallocateTrainees() {

    let param = {
      users: this.newArray,
    };

    this.managerResource
      .deallocateResources(0, this.managerId, this.userRole, param)
      .subscribe({
        next: (resp: any) => {
          this.deallocate = resp;
          this.toast.success(
            'Trainees sucessfully deallocated from manager',
            ''
          );
          this.newArray.length = 0;
          // this.assignedTrainees();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          } else {
            this.toast.error('An unexpected error has occured', '');
          }
        },
      });
  }

  onCheckboxChange(e: any, data: any) {
    if (e.target.checked) {
      this.newArray.push(data);
    } else {
      let removeIndex = this.newArray.findIndex((itm) => itm === data);
      if (removeIndex !== -1) this.newArray.splice(removeIndex, 1);
    }
  }
}
