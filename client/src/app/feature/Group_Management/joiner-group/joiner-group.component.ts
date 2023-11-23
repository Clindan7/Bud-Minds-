import { LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { BatchService } from 'src/app/core/services/batch.service';
import { GroupServiceService } from 'src/app/core/services/group-service.service';

@Component({
  selector: 'app-joiner-group',
  templateUrl: './joiner-group.component.html',
  styleUrls: ['./joiner-group.component.css']
})
export class JoinerGroupComponent implements OnInit {

  joinerGroup: any;
  batches: any;
  updateMode: boolean = false;
  submitted: boolean = false;

  @Input() groupId: number;

  groupForm = new FormGroup({
    groupName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$'),
      Validators.maxLength(20),
    ]),
    batch: new FormControl('', [Validators.required]),
  });

  constructor(
    private batchService: BatchService,
    private groupService: GroupServiceService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal,
    private location: LocationStrategy
  ) {
    if(this.activeModal){
    history.pushState(null, null, window.location.href);
    this.location.onPopState(() => {
    window.onpopstate = function (e) { window.history.forward(); }
      this.activeModal.dismiss()
    });
    window.onpopstate = function (e) { window.history.back(); }
    }
  }

  ngOnInit(): void {
    if (this.groupId) {
      this.updateMode = true;
      this.getGroup();
    }
    this.getBatch();
  }

  onInputFocus(event: any) {
    event.target.classList.add('selected-input');
  }

  onInputBlur(event: any) {
    event.target.classList.remove('selected-input');
  }

  getBatch() {
    this.batchService.getBatch().subscribe({
      next: (resp: any) => {
        this.batches = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Batches not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getGroup() {
    this.groupService.viewGroup(this.groupId).subscribe({
      next: (resp: any) => {
        this.joinerGroup = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toast.error('Group not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        } else {
          this.toast.error('User has no permission to access this action', '');
        }
      },
    });
  }

  setDefault() {
    this.groupForm.patchValue({
      groupName: this.joinerGroup.joinerGroupName,
      batch: this.joinerGroup.joinerBatch.joinerBatchId,
    });
  }

  createGroup() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      this.submitted = false;
    }
    else {
      let param = {
        joinerGroupName: this.groupForm.value.groupName,
        joinerBatchId: this.groupForm.value.batch,
      };
      this.groupService.addGroup(param).subscribe({
        next: (resp: any) => {
          this.toast.success('Group Added Successfully', '');
          this.submitted = true;
          this.activeModal.close();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          } else if (err.error.errorCode == '1983') {
            this.toast.error('Group Name is required', '');
          } else if (err.error.errorCode == '1966') {
            this.toast.error('Group Name size must be between 1 and 20', '');
          } else if (err.error.errorCode == '1988') {
            this.toast.error('Invalid Group Name', '');
          } else if (err.error.errorCode == '1987') {
            this.toast.error(
              'This group name already exists, try another name.',
              ''
            );
          } else if (err.error.errorCode == '1969') {
            this.toast.error('Batch not found', '');
          } else if (err.error.errorCode == '1989') {
            this.toast.error('Invalid Batch', '');
          } else if (err.error.errorCode == '1990') {
            this.toast.error('Batch ID is required', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }

  updateGroup() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      this.submitted = false;
    }
    else {
      let param = {
        joinerGroupName: this.groupForm.value.groupName,
        joinerBatchId: this.groupForm.value.batch,
      };
      this.groupService.editGroup(param, this.groupId).subscribe({
        next: (resp: any) => {
          this.toast.success('Group Updated Successfully', '');
          this.submitted = true;
          this.activeModal.close();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          } else if (err.error.errorCode == '1960') {
            this.toast.error('Group Not Found', '');
          } else if (err.error.errorCode == '1983') {
            this.toast.error('Group Name is required', '');
          } else if (err.error.errorCode == '1966') {
            this.toast.error('Group Name size must be between 1 and 20', '');
          } else if (err.error.errorCode == '1988') {
            this.toast.error('Invalid Group Name', '');
          } else if (err.error.errorCode == '1987') {
            this.toast.error(
              'This group name already exists, try another name.',
              ''
            );
          } else if (err.error.errorCode == '1969') {
            this.toast.error('Batch not found', '');
          } else if (err.error.errorCode == '1989') {
            this.toast.error('Invalid Batch', '');
          } else if (err.error.errorCode == '1990') {
            this.toast.error('Batch ID is required', '');
          } else if (err.error.errorCode == "1908") {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }
}
