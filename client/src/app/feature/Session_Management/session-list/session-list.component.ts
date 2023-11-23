import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SessionServiceService } from 'src/app/core/services/session-service.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TrainingFormComponent } from '../../Training_Management/training-form/training-form.component';
import { SessionFormComponent } from '../session-form/session-form.component';
import { DatePipe, formatDate } from '@angular/common';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.css']
})
export class SessionListComponent implements OnInit {

  sessionData: any;
  page:any=1
  limit:any=10
  len:any
  modalRef:any;
  searchSessionName!: FormGroup;
  searchTerm = '';
  groups:any;
  trainers:any;
  trainings:any;
  technologies:any;
  blnChangePage:boolean=false;
  skeletonData = Array(10).fill({});
  public skeletonFlag = true;
  editChek:boolean=false;

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
    this.searchSession(this.page, this.limit);
    this.getTraining();
    this.getTechnology();
    this.getTrainer();
    this.getGroup();
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
    this.sessionService.getTechnology().subscribe({
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
        this.groups = resp.result;
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

  deleteSession(sessionId:any){
    {
        this.modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      this.modalRef.componentInstance.message = 'Are you sure you want to delete this session?';
      this.modalRef.componentInstance.title = 'Confirm Delete';

      this.modalRef.result.then((result) => {
        if (result === 'confirm') {
          this.sessionService.deleteSession(sessionId).subscribe({
          next: (res:any) => {
            this.toast.success('Session deleted successfully');
          },
          error: (err:any) => {
            if (err.error.errorCode == "1912") {
              this.toast.error('User not found', '');
            } else if (err.error.errorCode == "1093"){
              this.toast.error('User has no permission to access this action', '');
            }
          },
          complete: () => {
            this.searchSession(this.page,this.limit,this.searchSessionName.value);
          },
        })
        }
      }).catch((error) => {
      });
    }

  }

  createSession() {
    let modalRef = this.modalService.open(SessionFormComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.result.then(
      (result) => {
        this.searchSession(this.page,this.limit);
      },
      () => { });

    modalRef.hidden.subscribe(() => {
        this.getTraining();
      });
  }


  updateSession(sessionId:any) {
    let modalRef = this.modalService.open(SessionFormComponent, {
      centered: true,
      backdrop:'static'
    });
    modalRef.componentInstance.sessionId = sessionId;
    modalRef.componentInstance.updateMode = true;
    modalRef.result.then(
      (result) => {
        this.searchSession(this.page,this.limit);
      },
      () => { });
  }

  searchSession(page:any, limit:any, searchSession?:any){
    
    if((this.searchSessionName.value.trainingId!=""||this.searchSessionName.value.technologyId!=""||this.searchSessionName.value.trainerId!=""||this.searchSessionName.value.groupId!="") && !this.blnChangePage){      
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

  editCheck(taskEnd:any){
    let date1=formatDate(new Date(new Date()),'yyyy-MM-dd','en_US');
    let date2=formatDate(new Date(taskEnd),'yyyy-MM-dd','en_US');
    console.log(date1,"    ",date2);
    
    if(date2<=date1){
      this.editChek=false;
    }
    else{
     this.editChek=true;
  }
}

}

