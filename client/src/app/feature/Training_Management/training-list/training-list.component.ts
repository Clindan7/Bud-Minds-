import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { TrainingServiceService } from 'src/app/core/services/training-service.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TrainingFormComponent } from '../training-form/training-form.component';

@Component({
  selector: 'app-training-list',
  templateUrl: './training-list.component.html',
  styleUrls: ['./training-list.component.css']
})
export class TrainingListComponent implements OnInit {

  trainingData: any;
  page:any=1
  limit:any=10
  len:any
  modalRef:any;
  searchTrainingName!: FormGroup;
  searchTerm = '';
  technologies:any;
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;

  constructor(private trainingService: TrainingServiceService,
    private router: Router,
    private toast: ToastrService,
    private modalService:NgbModal,
    private actRout: ActivatedRoute,
    private skeletonLoaderService: SkeletonLoaderService) { }

  ngOnInit(): void {
    this.searchTrainingName = new FormGroup({
      search: new FormControl(""),
      departmentId :new FormControl(""),
      technologyId :new FormControl("")
    })
    this.searchTraining(this.page, this.limit);
    // this.getCurrentUser();
    this.getTechnology();
  }

  getTechnology() {
    this.trainingService.getTechnology().subscribe({
      next: (resp: any) => {
        this.technologies = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Technology not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  deleteTraining(trainingId:any){
    {
        this.modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      this.modalRef.componentInstance.message = 'Are you sure you want to delete this training?';
      this.modalRef.componentInstance.title = 'Confirm Delete';

      this.modalRef.result.then((result) => {
        if (result === 'confirm') {
          this.trainingService.deleteTraining(trainingId).subscribe({
          next: (res:any) => {
            this.toast.success('Training deleted successfully');
          },
          error: (err:any) => {
            if (err.error.errorCode == "1912") {
              this.toast.error('User not found', '');
            } else if (err.error.errorCode == "1093"){
              this.toast.error('User has no permission to access this action', '');
            }
          },
          complete: () => {
            this.searchTraining(this.page,this.limit,this.searchTrainingName.value);
          },
        })
        }
      }).catch((error) => {
      });
    }

  }

  createTraining() {
    let modalRef = this.modalService.open(TrainingFormComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.result.then(
      (result) => {
        this.searchTraining(this.page,this.limit);
      },
      () => { });

    modalRef.hidden.subscribe(() => {
        this.getTechnology();
      });
  }


  updateTraining(trainingId:any) {
    let modalRef = this.modalService.open(TrainingFormComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.componentInstance.trainingId = trainingId;
    modalRef.componentInstance.updateMode = true;
    modalRef.result.then(
      (result) => {
        this.searchTraining(this.page,this.limit);
      },
      () => { });
  }

  searchTraining(pageN:any, pageL:any, searchTraining?:any){

    if((this.searchTrainingName.value.search!=""||this.searchTrainingName.value.technologyId!=""||this.searchTrainingName.value.departmentId!="")){
      this.page=1
    }

    this.trainingService.getTraining(this.page,this.limit,this.searchTrainingName.value).subscribe({
      next: (res:any)=>{
      this.len=res.numItems;
      this.trainingData = res.result;
      this.toggleSkeleton(false);
      },
      error: (err:any) => {
        if (err.error.errorCode == "1912"){
          this.toast.error('Training not found', '');
        } else if (err.error.errorCode == "1934"){
          this.toast.error('Invalid Department', '');
        } else if(err.error.errorCode == "1931"){
          this.toast.error('Page Number should be a positive integer', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
        this.trainingData=[];
      }
    })
  }

  toggleSkeleton(flag: boolean): void {
    this.skeletonFlag = flag;
    this.skeletonLoaderService.setLoading(flag);
  }

  clearSearchTerm(): void {
    this.searchTrainingName.controls['search'].reset();
    this.searchTrainingName.value.search="";
  }

  onPageChange($event) {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });

    this.page = $event;
    this.searchTraining(this.page,this.limit,this.searchTrainingName.value)
  }

}
