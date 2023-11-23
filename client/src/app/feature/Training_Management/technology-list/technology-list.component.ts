import { LocationStrategy } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TrainingServiceService } from 'src/app/core/services/training-service.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TechnologyComponent } from '../technology/technology.component';

@Component({
  selector: 'app-technology-list',
  templateUrl: './technology-list.component.html',
  styleUrls: ['./technology-list.component.css']
})
export class TechnologyListComponent implements OnInit {

  len:any
  technologyData: any;
  page:any;
  limit:any;
  modalRef:any;
  @Input() isSuccess: boolean=false;
  @Output() isAdded: EventEmitter<any> = new EventEmitter<any>();



  constructor(public activeModal: NgbActiveModal,
    private trainingService: TrainingServiceService,
    private router: Router,
    private modalService: NgbModal,
    private toast: ToastrService,
    private actRout: ActivatedRoute,
    private location: LocationStrategy) {
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

  ngOnInit(): void {
    this.getTechnology();
  }

  getTechnology(){

    this.trainingService.getTechnology().subscribe({
      next: (res:any)=>{
      this.technologyData = res;

      },
      error: (err:any) => {
        if (err.error.errorCode == "1912"){
          this.toast.error('Training not found', '');
        } else if (err.error.errorCode == "1934"){
          this.toast.error('Invalid Department', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        } else if(err.error.errorCode == "1931"){
          this.toast.error('Department id should be a positive integer', '');
        }
        this.technologyData=[];
      }
    })
  }
  deleteTechnologyDetails(technologyId:any){
    {
      this.modalRef = this.modalService.dismissAll();
        this.modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      this.modalRef.componentInstance.message = 'Are you sure you want to delete this technology?';
      this.modalRef.componentInstance.title = 'Confirm Delete';

      this.modalRef.result.then((result) => {
        if (result === 'confirm') {
          this.trainingService.deleteTechnology(technologyId).subscribe({
          next: (res:any) => {
            this.toast.success('Technology deleted successfully');
          },
          error: (err:any) => {
            if (err.error.errorCode == "1912") {
              this.toast.error('User not found', '');
            } else if (err.error.errorCode == "1093"){
              this.toast.error('User has no permission to access this action', '');
            }
          },
          complete: () => {
            this.modalService.open(TechnologyListComponent, {
              centered: true,
            });
          },
        })
        }
      }).catch((error) => {
      });
    }

  }

  //   close(){
  // this.location.back();
  // }

  openCreateModal() {
    {
      // this.modalRef = this.modalService.dismissAll();
      const modal = this.modalService.open(TechnologyComponent, {
        centered: true,
        backdrop:'static'
      });
      // modal.componentInstance.inputData = technology;
      modal.result.then(
        (result) => {


          this.getTechnology();

        },
        () => {}
      );
    }
  }
}
