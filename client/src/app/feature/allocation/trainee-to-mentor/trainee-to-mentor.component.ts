import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MentorResourceAllocationService } from 'src/app/core/services/mentor-resource-allocation.service';

@Component({
  selector: 'app-trainee-to-mentor',
  templateUrl: './trainee-to-mentor.component.html',
  styleUrls: ['./trainee-to-mentor.component.css'],
})
export class TraineeToMentorComponent implements OnInit {
  constructor(
    private mentorResource: MentorResourceAllocationService,
    private route: ActivatedRoute,
    private toast: ToastrService
  ) {}

  active: number = 1;
  users: any;
  allocate: any;
  deallocate: any;
  mentorId: any;
  userRole: any = 4;
  newArray: Array<number>[] = [];
  checked: any = 0;

  ngOnInit(): void {
    this.mentorId = this.route.snapshot.params['id'];
    // this.assignedTrainees();
    // this.totalTrainees();
  }

  // assignedTrainees() {
  //   this.newArray.length = 0;
  //   this.mentorResource.assignedResources(this.mentorId).subscribe({
  //     next: (resp: any) => {
  //       this.deallocate = resp;
  //     },
  //     error: (err: any) => {
  //       if (err.error.errorCode == '1093') {
  //         this.toast.error('User has no permission to access this action', '');
  //       } else if (err.error.errorCode == '1997') {
  //         this.toast.error('Mentor not found', '');
  //       } else {
  //         this.toast.error('An unexpected error has occured', '');
  //       }
  //     },
  //   });
  // }

  // totalTrainees() {
  //   this.newArray.length = 0;
  //   this.mentorResource.unassignedResources().subscribe({
  //     next: (resp: any) => {
  //       this.allocate = resp;
  //     },
  //     error: (err: any) => {
  //       if (err.error.errorCode == '1093') {
  //         this.toast.error('User has no permission to access this action', '');
  //       } else if (err.error.errorCode == '1997') {
  //         this.toast.error('Mentor not found', '');
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

    this.mentorResource.allocateResources(1, this.mentorId, param).subscribe({
      next: (resp: any) => {
        this.allocate = resp;
        this.toast.success('Trainees sucessfully allocated to manager', '');
        this.newArray.length = 0;
        // this.totalTrainees();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to perform this action', '');
        } else if (
          err.error.errorCode == '1940' &&
          err.error.errorMessage.slice(0, 40) ==
            'Manager is not assigned for the trainees'
        ) {
          this.toast.error('Manager is not assigned for the trainees', '');
        } else if (err.error.errorCode == '1940') {
          this.toast.error(
            'The mentor user is either deallocated already or not found',
            ''
          );
        } else if (
          err.error.errorCode == '1940' &&
          err.error.errorMessage == 'Mentor is already deallocated'
        ) {
          this.toast.error('Mentor is already deallocated', '');
        } else if (err.error.errorCode == '1912') {
          this.toast.error('User not found', '');
        } else if (
          err.error.errorCode == '1940' &&
          err.error.errorMessage == 'Manager is not assigned for the trainees'
        ) {
          this.toast.error('Manager is not assigned for the trainees', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage == 'Users should be of type java.util.List'
        ) {
          this.toast.error('Users should be of type list', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage ==
            'mentorId should be of type type java.util.Integer'
        ) {
          this.toast.error('mentorId should be of type Integer', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage == 'AllocationMode should be of type byte'
        ) {
          this.toast.error('AllocationMode should be of type byte', '');
        } else if (err.error.errorCode == '1994') {
          this.toast.error('Users not selected', '');
        } else if (err.error.errorCode == '1941') {
          this.toast.error('Invalid allocation mode', '');
        } else if (err.error.errorCode == '1946') {
          this.toast.error('Mentor ID is required', '');
        } else if (err.error.errorCode == '1997') {
          this.toast.error('Mentor not found', '');
        } else {
          this.toast.error('An unexpected error has occured', '');
        }
      },
    });
  }

  deallocateTrainees() {
    let param = {
      users: this.newArray,
    };

    this.mentorResource.deallocateResources(0, this.mentorId, param).subscribe({
      next: (resp: any) => {
        this.deallocate = resp;
        this.toast.success('Trainees sucessfully deallocated from manager', '');
        this.newArray.length = 0;
        // this.assignedTrainees();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to perform this action', '');
        } else if (err.error.errorCode == '1940') {
          this.toast.error(
            'The mentor user is either deallocated already or not found',
            ''
          );
        } else if (err.error.errorCode == '1912') {
          this.toast.error('User not found', '');
        } else if (
          err.error.errorCode == '1940' &&
          err.error.errorMessage == 'Manager is not assigned for the trainees'
        ) {
          this.toast.error('Manager is not assigned for the trainees', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage == 'Users should be of type java.util.List'
        ) {
          this.toast.error('Users should be of type list', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage ==
            'mentorId should be of type type java.util.Integer'
        ) {
          this.toast.error('mentorId should be of type Integer', '');
        } else if (
          err.error.errorCode == '1931' &&
          err.error.errorMessage == 'AllocationMode should be of type byte'
        ) {
          this.toast.error('AllocationMode should be of type byte', '');
        } else if (err.error.errorCode == '1994') {
          this.toast.error('Users not selected', '');
        } else if (err.error.errorCode == '1941') {
          this.toast.error('Invalid allocation mode', '');
        } else if (err.error.errorCode == '1946') {
          this.toast.error('Mentor ID is required', '');
        } else if (err.error.errorCode == '1997') {
          this.toast.error('Mentor not found', '');
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
