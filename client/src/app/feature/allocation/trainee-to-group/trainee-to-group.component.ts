import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GroupAllocationService } from 'src/app/core/services/group-allocation.service';

@Component({
  selector: 'app-trainee-to-group',
  templateUrl: './trainee-to-group.component.html',
  styleUrls: ['./trainee-to-group.component.css'],
})
export class TraineeToGroupComponent implements OnInit {
  constructor(
    private groupAllocation: GroupAllocationService,
    private route: ActivatedRoute,
    private toast: ToastrService
  ) {}

  active: number = 1;
  users: any;
  allocate: any;
  deallocate: any;
  groupId: any;
  userRole: any = 4;
  newArray: Array<number>[] = [];
  checked: any = 0;

  ngOnInit(): void {
    this.groupId = this.route.snapshot.params['id'];
    // this.assignedTrainees();
    // this.totalTrainees();
  }

  // assignedTrainees() {
  //   this.newArray.length = 0;
  //   this.groupAllocation
  //     .assignedResources(this.groupId)
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
  //         } else if (err.error.errorCode == '1960') {
  //           this.toast.error('Group is not found', '');
  //         } else if (err.error.errorCode == '1931') {
  //           this.toast.error(
  //             'joinerGroupId should be of type java.lang.Integer',
  //             ''
  //           );
  //         }
  //         else if (err.error.errorCode == '1908') {
  //           this.toast.error('Invalid token', '');
  //         }
  //         else if (err.error.errorCode == '1909') {
  //           this.toast.error('Authorization token expired', '');
  //         }else {
  //           this.toast.error('An unexpected error has occured', '');
  //         }
  //       },
  //     });
  // }

  // totalTrainees() {
  //   this.newArray.length = 0;
  //   this.groupAllocation.unassignedResources().subscribe({
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

    this.groupAllocation
      .allocateResources(1, this.groupId, this.userRole, param)
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
          } else if (err.error.errorCode == '1960') {
            this.toast.error('group not found', '');
          } else if (err.error.errorCode == '1931') {
            this.toast.error(
              'joinerGroupId should be of type java.lang.Integer',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error('User not found - User IDs:[91, 92].', '');
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'User not found - User IDs:[91, 92]. Group is already allocated - User IDs:[31, 32]. ',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'User not found - User IDs:[94, 95]. Group is already deallocated - User IDs:[41, 42]. ',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'Group is already allocated - User IDs:[31, 32].',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'Group is already deallocated - User IDs:[31, 32]. ',
              ''
            );
          } else if (err.error.errorCode == '1999') {
            this.toast.error('GroupId not required', '');
          } else if (err.error.errorCode == '1944') {
            this.toast.error('GroupId is required', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1931') {
            this.toast.error('allocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          }
          else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          }else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
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

    this.groupAllocation
      .deallocateResources(0, this.userRole, param)
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
          } else if (err.error.errorCode == '1960') {
            this.toast.error('group not found', '');
          } else if (err.error.errorCode == '1931') {
            this.toast.error(
              'joinerGroupId should be of type java.lang.Integer',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error('User not found - User IDs:[91, 92].', '');
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'User not found - User IDs:[91, 92]. Group is already allocated - User IDs:[31, 32]. ',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'User not found - User IDs:[94, 95]. Group is already deallocated - User IDs:[41, 42]. ',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'Group is already allocated - User IDs:[31, 32].',
              ''
            );
          } else if (err.error.errorCode == '1943') {
            this.toast.error(
              'Group is already deallocated - User IDs:[31, 32]. ',
              ''
            );
          } else if (err.error.errorCode == '1999') {
            this.toast.error('GroupId not required', '');
          } else if (err.error.errorCode == '1944') {
            this.toast.error('GroupId is required', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1931') {
            this.toast.error('allocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
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
