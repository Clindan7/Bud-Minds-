import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SessionServiceService } from 'src/app/core/services/session-service.service';
import { SkeletonLoaderService } from 'src/app/core/services/skeleton-loader.service';
import { TaskServiceService } from 'src/app/core/services/task-service.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TaskFormComponent } from '../task-form/task-form.component';

@Component({
  selector: 'app-trainee-task-list',
  templateUrl: './trainee-task-list.component.html',
  styleUrls: ['./trainee-task-list.component.css']
})
export class TraineeTaskListComponent implements OnInit {

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

  constructor(
    private taskService: TaskServiceService,
    private sessionService: SessionServiceService,
    private router: Router,
    private datePipe: DatePipe,
    private modalService:NgbModal,
    private toast: ToastrService,
    private skeletonLoaderService: SkeletonLoaderService
  ) {}

  ngOnInit(): void {
    this.searchTaskName = new FormGroup({
      trainingId: new FormControl(''),
      taskId: new FormControl(''),
      technologyId: new FormControl(''),
    });
    this.getTask();
    this.getTraining();
    this.getTechnology();
    this.searchTask(this.page, this.limit);
  }

  searchTask(page: any, limit: any, searchTask?: any) {

    if((this.searchTaskName.value.trainingId!=""||this.searchTaskName.value.technologyId!=""||this.searchTaskName.value.taskId!="") && !this.blnChangePage){      
      this.page=1
      page=1
      this.blnChangePage=false
    }

    this.taskService
      .getTraineeTask(page, limit, this.searchTaskName.value)
      .subscribe({
        next:(res: any) => {   
          this.len = res.numItems;
          this.taskData = res.result; 
          this.toggleSkeleton(false);
          this.blnChangePage=false;
        },
        error: (err: any) => {
          console.log(err, 'error');
          if (err.error.errorCode == "1913"){
            this.toast.error('Task not found', '');
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
    this.sessionService.getTrainingList().subscribe({
      next: (resp: any) => {
        this.trainings = resp.result;
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
    this.taskService.getTraineeTask(1,20000).subscribe({
      next: (resp: any) => {
        this.tasks = resp.result;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Tasks not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  getTechnology() {
    this.taskService.getTechnologyList().subscribe({
      next: (resp: any) => {
        this.technologies = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Technologies not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  createTask() {
    let modalRef = this.modalService.open(TaskFormComponent, {
      centered: true,
      backdrop: 'static',
    });
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
      this.modalRef.componentInstance.message = 'Are you sure you want to delete this task?';
      this.modalRef.componentInstance.title = 'Confirm Delete';

      this.modalRef.result.then((result) => {
        if (result === 'confirm') {
          this.taskService.deleteTask(taskId).subscribe({
          next: (res:any) => {
            this.toast.success('Task deleted successfully');
          },
          error: (err:any) => {
            if (err.error.errorCode == "1913") {
              this.toast.error('Task not found', '');
            } else if (err.error.errorCode == "1915"){
              this.toast.error('This task is already deleted', '');
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
}

