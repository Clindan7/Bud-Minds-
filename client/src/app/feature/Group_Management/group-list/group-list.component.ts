import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { BatchService } from 'src/app/core/services/batch.service';
import { GroupServiceService } from 'src/app/core/services/group-service.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { JoinerGroupComponent } from '../joiner-group/joiner-group.component';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css']
})
export class GroupListComponent implements OnInit {

  page: any = 1;
  limit: any = 10;
  len: any;
  batches: any;
  groupData: any;
  searchTerm = '';
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;

  searchGroupName: FormGroup = new FormGroup({
    groupName: new FormControl(''),
    batchName: new FormControl(''),
  });

  constructor( private groupService: GroupServiceService,
    private batchService: BatchService,
    private router: Router,
    private toast: ToastrService,
    private modalService: NgbModal,
    private actRout: ActivatedRoute,
    private skeletonLoaderService: SkeletonLoaderService) { }

  ngOnInit(): void {
    this.searchGroup(this.page, this.limit);
    this.getBatch();
  }

  getBatch() {
    this.batchService.getBatch().subscribe({
      next: (resp: any) => {
        this.batches = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Batches not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  searchGroup(pageN: any, pageL: any, searchGrp?: any) {
    if((this.searchGroupName.value.groupName!=""||this.searchGroupName.value.batchName!="")){
      this.page=1
    }

    this.groupService
      .getGroups(this.page, this.limit, this.searchGroupName.value)
      .subscribe({
        next: (res: any) => {
          this.len = res.numItems;
          this.groupData = res.result;
          this.toggleSkeleton(false);
        },
        error: (err: any) => {
          if (err.error.errorCode == '1960') {
            this.toast.error('Group not found', '');
          } else if (err.error.errorCode == '1969') {
            this.toast.error('Group not found', '');
          } else if(err.error.errorCode == "1931"){
            this.toast.error('Page Number should be a positive integer', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
          this.groupData = [];
        },
      });
  }

  toggleSkeleton(flag: boolean): void {
    this.skeletonFlag = flag;
    this.skeletonLoaderService.setLoading(flag);
  }

  onPageChange($event) {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
    this.page = $event;
    this.searchGroup(this.page, this.limit, this.searchGroupName.value);
  }

  clearSearchTerm(): void {
    this.searchGroupName.controls['groupName'].reset();
    this.searchGroupName.value.groupName="";
  }

  createGroup() {
    const modal = this.modalService.open(JoinerGroupComponent, {
      centered: true,
      backdrop:'static'
    });
    modal.result.then(
      (result) => {
        this.searchGroup(this.page, this.limit, this.searchGroupName.value);
      },
      () => {}
    );
  }

  updateGroup(groupId: number) {
    const modalRef = this.modalService.open(JoinerGroupComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.componentInstance.groupId = groupId;
    modalRef.result.then(
      (result) => {
        this.searchGroup(this.page, this.limit, this.searchGroupName.value);
      },
      () => {}
    );
  }
  deleteGroup(groupId: any) {
    {
      const modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      modalRef.componentInstance.message =
        'Are you sure you want to delete this group?';
      modalRef.componentInstance.title = 'Confirm Delete';

      modalRef.result
        .then((result) => {
          if (result === 'confirm') {
            this.groupService.deleteGroup(groupId).subscribe({
              next: (res: any) => {
                this.toast.success('Group deleted successfully');
              },
              error: (err: any) => {
                if (err.error.errorCode == '1093') {
                  this.toast.error(
                    'User has no permission to access this action',
                    ''
                  );
                } else if (err.error.errorCode == '1960') {
                  this.toast.error('Group Not Found', '');
                } else if (err.error.errorCode == '1962') {
                  this.toast.error('This group is not empty', '');
                } else if (err.error.errorCode == '1961') {
                  this.toast.error('Group is already deleted', '');
                }
              },
              complete: () => {
                this.searchGroup(
                  this.page,
                  this.limit,
                  this.searchGroupName.value
                );
              },
            });
          }
        })
        .catch((error) => {});
    }
  }
}
