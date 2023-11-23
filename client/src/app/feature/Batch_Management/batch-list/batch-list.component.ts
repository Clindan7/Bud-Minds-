import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { BatchService } from 'src/app/core/services/batch.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { ViewModalComponent } from 'src/app/shared/components/view-modal/view-modal.component';
import { JoinerBatchComponent } from '../joiner-batch/joiner-batch.component';

@Component({
  selector: 'app-batch-list',
  templateUrl: './batch-list.component.html',
  styleUrls: ['./batch-list.component.css']
})
export class BatchListComponent implements OnInit {

  batches: any;
  keyword = 'batchName';
  page: any = 1;
  limit: any = 10;
  len: any;
  batchData: any;
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;

  modalOpen : boolean = false;
  confirmModalOpen : boolean = false;

  constructor(private batchservice: BatchService,
    private toast: ToastrService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    private actRout: ActivatedRoute,
    private router: Router,
    private skeletonLoaderService: SkeletonLoaderService) { }

    searchBatchName: FormGroup = this.fb.group({
      batch: '',
    });

  ngOnInit(): void {

    this.searchBatch(this.page, this.limit);
    this.findbatch();
  }

  findbatch() {
    this.batchservice.getBatches().subscribe({
      next: (resp: any) => {
        this.batches = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to access this action', '');
        }
        if (err.error.errorCode == '1969') {
          this.toast.error('Batch not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  searchBatch(pageN:any, pageL:any, searchBtch?:any) {

    if((this.searchBatchName.value.batch!="")){
      this.page=1
    }

    this.batchservice
      .findBatches(this.page, this.limit, this.searchBatchName.value)
      .subscribe({
        next: (res: any) => {
          this.len = res.numItems;
          this.batchData = res.result;
          this.toggleSkeleton(false);
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          }
          if (err.error.errorCode == '1969') {
            this.toast.error('Batch not found', '');
          } else if(err.error.errorCode == "1931"){
            this.toast.error('Page Number should be a positive integer', '');
          } else if (err.error.errorCode == "1908") {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
          this.batchData = [];
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
    this.searchBatch(this.page, this.limit, this.searchBatchName.value);
  }

  createBatch() {
    {
      const modal = this.modalService.open(JoinerBatchComponent, {
        centered: true,
        backdrop:'static'
      });
      modal.result.then(
        (result) => {
          this.searchBatch(this.page, this.limit, this.searchBatchName.value);
        },
        () => {}
      );
    }
  }

  viewBatch(batchId: any) {
    const modalRef = this.modalService.open(ViewModalComponent, {
      centered: true,
    });
    modalRef.componentInstance.id = batchId;
  }

  updateBatch(batchId: any) {
    const modalRef = this.modalService.open(JoinerBatchComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.componentInstance.batchId = batchId;
    modalRef.result.then(
      (result) => {
        this.searchBatch(this.page, this.limit, this.searchBatchName.value);
      },
      () => {}
    );
  }

  deleteBatch(batchId: any) {
    {
      const modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      modalRef.componentInstance.message =
        'Are you sure you want to delete this batch?';
      modalRef.componentInstance.title = 'Confirm Delete';

      modalRef.result
        .then((result) => {
          if (result === 'confirm') {
            this.batchservice.deleteBatch(batchId).subscribe({
              next: (res: any) => {
                this.toast.success('Batch deleted successfully');
              },
              error: (err: any) => {
                if (err.error.errorCode == '1093') {
                  this.toast.error(
                    'User has no permission to access this action',
                    ''
                  );
                } else if (err.error.errorCode == '1969') {
                  this.toast.error('Batch not found', '');
                } else if (err.error.errorCode == '1971') {
                  const Swal = require('sweetalert2');
                  Swal.fire({
                    title: 'Oops!',
                    text: 'Batch containing active groups so cannot be deleted',
                    icon: 'error',
                    confirmButtonText: 'Ok',
                    confirmButtonColor: '#104472',
                  });
                } else if (err.error.errorCode == '1972') {
                  this.toast.error('Batch is already deleted', '');
                } else if (err.error.errorCode == "1908") {
                  this.toast.error('Invalid token', '');
                } else if (err.error.errorCode == '1909') {
                  this.toast.error('Authorization token expired', '');
                }
              },
              complete: () => {
                this.searchBatch(
                  this.page,
                  this.limit,
                  this.searchBatchName.value
                );
              },
            });
          }
        })
        .catch((error) => {});
    }
  }

  launchModal(): void{
    this.modalOpen = !this.modalOpen;
  }

  launchConfirmModal(): void{
    this.confirmModalOpen = !this.confirmModalOpen;
  }

}
