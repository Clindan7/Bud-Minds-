import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { DatePipe, formatDate } from '@angular/common';
import { SubtaskService } from 'src/app/core/services/subtask-service.service';
import { SubtaskFormComponent } from '../subtask-form/subtask-form.component';

@Component({
  selector: 'app-subtask-management',
  templateUrl: './subtask-management.component.html',
  styleUrls: ['./subtask-management.component.css']
})
export class SubtaskManagementComponent implements OnInit {
  page: any = 1;
  limit: any = 10;
  len: any;
  searchTaskName!: FormGroup;
  taskData: any;
  modalRef:any;
  public skeletonFlag = true;
  skeletonData = Array(10).fill({});
  blnChangePage:boolean=false;
  tasks:any;
  trainings:any;
  technologies:any;
  taskMode:any=0;
  editChek:boolean=false;

  constructor(private router: Router,private subtaskService: SubtaskService  ,private toast: ToastrService,private skeletonLoaderService: SkeletonLoaderService,private datePipe: DatePipe,private modalService:NgbModal) { }

  ngOnInit(): void {
    this.searchTaskName = new FormGroup({
      trainingId: new FormControl(''),
      taskId: new FormControl(''),
      search: new FormControl(''),
    });
    this.getTask();
    this.getTraining();
    this.getTechnology();
    this.searchTask(this.page, this.limit);
  }

  searchTask(page: any, limit: any, searchTask?: any) {

    if((this.searchTaskName.value.trainingId!=""||this.searchTaskName.value.search!=""||this.searchTaskName.value.taskId!="") && !this.blnChangePage){      
      this.page=1
      page=1
      this.blnChangePage=false
    }

    this.subtaskService
      .getSubtask(page, limit,1, this.searchTaskName.value)
      .subscribe({
        next:(res: any) => {
          console.log(res,"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
          
          this.len = res.numItems;
          this.taskData = res.result; 
          this.toggleSkeleton(false);
          this.blnChangePage=false;
        },
        error: (err: any) => {
          console.log(err, 'error');
          if (err.error.errorCode == "1913"){
            this.toast.error('Subtask not found', '');
          } else if (err.error.errorCode == "1908") {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
          this.taskData=[];
        },
      });
  }

  getTraining() {
    this.subtaskService.getTrainingList().subscribe({
      next: (resp: any) => {
        this.trainings = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Trainings not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getTask() {
    this.subtaskService.getTaskList().subscribe({
      next: (resp: any) => {
        this.tasks = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Subtask not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getTechnology() {
    // this.taskService.getTechnologyList().subscribe({
    //   next: (resp: any) => {
    //     this.technologies = resp;
    //   },
    //   error: (err: any) => {
    //     if (err.error.errorCode == '1960') {
    //       this.toast.error('Technologies not found', '');
    //     } else if (err.error.errorCode == "1930") {
    //       this.toast.error('Invalid token', '');
    //     } else if (err.error.errorCode == '1909') {
    //       this.toast.error('Authorization token expired', '');
    //     }
    //   },
    // });
  }

  createTask() {
    let modalRef = this.modalService.open(SubtaskFormComponent, {
      centered: true,
      backdrop: 'static',
    });
    // this.searchTask(this.page, this.limit);
    modalRef.result.then(
      (result) => {
        this.searchTask(this.page, this.limit);
      },
      () => {}
    );

    modalRef.hidden.subscribe(() => {
      this.getTraining();
      this.getTechnology();
    });
  }

  toggleSkeleton(flag: boolean): void {
    this.skeletonFlag = flag;
    this.skeletonLoaderService.setLoading(flag);
  }

  formatDateTime(dateTime: Date): string {
    return this.datePipe.transform(dateTime, 'yyyy-MM-dd HH:mm');
  }

  deleteTask(taskId:any){
    {
        this.modalRef = this.modalService.open(ConfirmationModelComponent, {
        centered: true,
        backdrop:'static'
      });
      this.modalRef.componentInstance.message = 'Are you sure you want to delete this subtask?';
      this.modalRef.componentInstance.title = 'Confirm Delete';

      this.modalRef.result.then((result) => {
        if (result === 'confirm') {
          this.subtaskService.deleteTask(taskId).subscribe({
          next: (res:any) => {
            this.toast.success('Subtask deleted successfully');
          },
          error: (err:any) => {
            if (err.error.errorCode == "1913") {
              this.toast.error('Subtask not found', '');
            } else if (err.error.errorCode == "1915"){
              this.toast.error('This subtask is already deleted', '');
            } else if (err.error.errorCode == "1093"){
              this.toast.error('User has no permission to access this action', '');
            } 
          },
          complete: () => {
            this.searchTask(this.page,this.limit,this.searchTaskName.value);
          },
        })
        }
      }).catch((error) => {
      });
    }

  }

  onPageChange($event) {
    this.blnChangePage=true;
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });

    this.page = $event;
    this.searchTask(this.page,this.limit,this.searchTaskName.value);
  }

  updateTask(taskId:any) {
    // let modalRef = this.modalService.open(TaskFormComponent, {
    //   centered: true,
    //   backdrop:'static'
    // });
    // console.log(taskId);
    
    // modalRef.componentInstance.taskId = taskId;
    // modalRef.componentInstance.updateMode = true;
    // modalRef.result.then(
    //   (result) => {
    //     this.searchTask(this.page,this.limit);
    //   },
    //   () => { });
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

clearSearchTerm(): void {
  this.searchTaskName.controls['search'].reset();
  this.searchTaskName.value.search="";
}

}
