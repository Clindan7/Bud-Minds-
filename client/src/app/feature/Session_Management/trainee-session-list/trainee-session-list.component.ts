import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SessionServiceService } from 'src/app/core/services/session-service.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { SessionFormComponent } from '../session-form/session-form.component';

@Component({
  selector: 'app-trainee-session-list',
  templateUrl: './trainee-session-list.component.html',
  styleUrls: ['./trainee-session-list.component.css']
})
export class TraineeSessionListComponent implements OnInit {

  sessionData: any;
  page:any=1
  limit:any=10
  len:any
  modalRef:any;
  searchSessionName!: FormGroup;
  searchTerm = '';
  technologies:any;
  trainers:any;
  trainings:any;
  blnChangePage:boolean=false;
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;

  constructor(private sessionService: SessionServiceService,
    private router: Router,
    private toast: ToastrService,
    private modalService:NgbModal,
    private actRout: ActivatedRoute,
    private datePipe: DatePipe,
    private skeletonLoaderService: SkeletonLoaderService) { }

  ngOnInit(): void {
    this.searchSessionName = new FormGroup({
      trainingId:new FormControl(""),
      technologyId:new FormControl(""),
      trainerId: new FormControl(""),
      groupId: new FormControl("")
    })
    this.getTraining();
    this.getTechnology();
    this.getTrainer();
    this.searchSession(this.page, this.limit);
  }

  formatDateTime(dateTime: Date): string {
    return this.datePipe.transform(dateTime, 'yyyy-MM-dd HH:mm');
  }

  getTraining() {
    this.sessionService.getTrainingList().subscribe({
      next: (resp: any) => {
        this.trainings = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Training not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getTechnology() {
    this.sessionService.getTechnologyList().subscribe({
      next: (resp: any) => {
        this.technologies = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Training not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getTrainer() {
    this.sessionService.getTrainerList().subscribe({
      next: (resp: any) => {
        this.trainers = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Trainer not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getGroup() {
    this.sessionService.getGroups(1,20000).subscribe({
      next: (resp: any) => {
        this.technologies = resp.result;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Group not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  searchSession(page:any, limit:any, searchSession?:any){

    if((this.searchSessionName.value.trainingId!=""||this.searchSessionName.value.trainerId!=""||this.searchSessionName.value.technologyId!="")&& !this.blnChangePage){
      this.page=1
      page=1
      this.blnChangePage=false
    }

    this.sessionService.getSession(page,limit,this.searchSessionName.value).subscribe({
      next: (res:any)=>{
      this.len=res.numItems;
      this.sessionData = res.result;
      this.toggleSkeleton(false);
      this.blnChangePage=false
      },
      error: (err:any) => {
        if (err.error.errorCode == "1979"){
          this.toast.error('Session not found', '');
        } else if(err.error.errorCode == "1931"){
          this.toast.error('Page Number should be a positive integer', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
        this.sessionData=[];
      }
    })
  }

  toggleSkeleton(flag: boolean): void {
    this.skeletonFlag = flag;
    this.skeletonLoaderService.setLoading(flag);
  }

  clearSearchTerm(): void {
    this.searchSessionName.controls['search'].reset();
    this.searchSessionName.value.search="";
  }

  onPageChange($event) {
    this.blnChangePage=true;
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });

    this.page = $event;
    this.searchSession(this.page,this.limit,this.searchSessionName.value)
  }

}


