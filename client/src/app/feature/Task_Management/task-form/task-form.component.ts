import { Component, OnInit, Input } from '@angular/core';
import { DatePipe, LocationStrategy, Time, formatDate } from '@angular/common';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {
  NgbModal,
  NgbActiveModal,
  NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TaskServiceService } from 'src/app/core/services/task-service.service';
import { SessionServiceService } from 'src/app/core/services/session-service.service';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css'],
})
export class TaskFormComponent implements OnInit {
  taskStart: any;
  taskEnd: any;
  minDate: any;
  trainings: any;
  modalRef: any;
  training: any;
  submitted: boolean = false;
  updateMode: boolean = false;
  task: any;

  @Input() taskId: number;
  @Input() isAdded: boolean = false;

  TaskForm = this.fb.group({
    training: new FormControl('', [Validators.required]),
    taskName: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^[^\\s]+([\\s][^\\s]+)*$'
      ),
      Validators.maxLength(50),
    ]),
    taskDescription: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^[^\\s]+([\\s][^\\s]+)*$'
      ),
      Validators.maxLength(250),
    ]),
    taskStart: new FormControl(''),
    taskEnd: new FormControl(''),
  });

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private taskService: TaskServiceService,
    private sessionService: SessionServiceService,
    private toastrService: ToastrService,
    private datepipe: DatePipe,
    private modalService: NgbModal,
    private activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.minDate = new Date();
    this.taskStart = new Date();
    this.taskEnd = new Date();
    this.TaskForm.patchValue({
      taskName: '',
      taskDescription: '',
      taskStart: this.taskStart,
      taskEnd: this.taskEnd,
    });
    this.getTraining();
    if (this.updateMode) {
      this.getTraining();
      this.DetailView();
    }
  }

  getTraining() {
    this.sessionService.getTrainingList().subscribe({
      next: (resp: any) => {
        this.trainings = resp;
        console.log("inside thisss");
        
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toastrService.error('Trainings not found', '');
        } else if (err.error.errorCode == '1930') {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
      },
    });
  }

  DetailView() {
    this.taskService.DetailTaskView(this.taskId).subscribe({
      next: (resp: any) => {
        console.log(resp);

        this.task = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1913') {
          this.toastrService.error('Task not found', '');
        } else if (err.error.errorCode == '1931') {
          this.toastrService.error(
            'Task Id should be of a positive Integer',
            ''
          );
        }
      },
    });
  }

  setDefault() {
    console.log(this.task, 'task');
    console.log(this.task.taskName);
    console.log(this.task.trainingId.trainingId);
  
    this.TaskForm.patchValue({
      training: this.task.trainingId.trainingId,
      taskName: this.task.taskName,
      taskDescription: this.task.taskDescription,
      taskStart: this.task.taskStart,
      taskEnd: this.task.taskEnd,
    });
  
    let date1 = formatDate(new Date(this.minDate), 'yyyy-MM-dd', 'en_US');
    let date2 = formatDate(new Date(this.task.taskStart), 'yyyy-MM-dd', 'en_US');
  
    if (date1 <= date2) {
      console.log("inside if");
      this.minDate = new Date(this.task.taskStart);
      this.TaskForm.patchValue({
        taskStart: this.task.taskStart,
        taskEnd: this.task.taskStart,
      });
    } else {
      console.log("inside else");
      this.TaskForm.patchValue({
        taskStart: this.minDate,
        taskEnd: this.minDate,
      });
    }
  
    console.log(this.TaskForm);
  }
  

  onInputFocus(event: any) {
    event.target.classList.add('selected-input');
  }

  onInputBlur(event: any) {
    event.target.classList.remove('selected-input');
  }

  errorMessages = {
    '1900': 'Invalid task name',
    '1901': 'Invalid task description',
    '1903': 'Task name is required',
    '1904': 'Task description is required',
    '1905': 'Training is required',
    '1906': 'Task name size must be between 1 and 50',
    '1907': 'Task description size must be between 1 and 250',
    '1912': 'Training not found',
    '1917': 'End date is required',
    '1916': 'Start date is required',
    '1955': 'Task start date invalid',
    '1948': 'Start date is invalid',
    '1945': 'End date is invalid',
    '1093': 'User has no permission to access this action',
    '1913': 'Task  not found'
  };

  createTask() {
    if (this.TaskForm.invalid) {
      this.submitted = false;
      this.TaskForm.markAllAsTouched();
    } else {
      let param = {
        trainingId: this.TaskForm.value.training,
        taskName: this.TaskForm.value.taskName,
        taskDescription: this.TaskForm.value.taskDescription,
        taskStart: this.datepipe.transform(
          this.TaskForm.value.taskStart,
          'yyyy-MM-dd'
        ),
        taskEnd: this.datepipe.transform(
          new Date(this.TaskForm.value.taskEnd),
          'yyyy-MM-dd'
        ),
      };
      this.submitted = true;
      this.taskService.addTask(param).subscribe({
        next: (resp: any) => {
          this.toastrService.success('Task added successfully', '');
          this.activeModal.close();
        },
        error: (err: any) => {
          this.submitted = false;
          this.submitted = false;
          const errorMessage = this.errorMessages[err.error.errorCode];

          this.toastrService.error(errorMessage, '');
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }

  start() {
    if (this.taskStart > this.taskEnd) {
      this.TaskForm.patchValue({
        taskEnd: this.taskStart,
      });
    }
  }

  close() {
    this.modalRef = this.modalService.dismissAll();
  }

  getStartandEndDate() {
    this.minDate = new Date();
    // this.minDate = moment().format('YYYY-MM-DD')
    let trainingId = this.TaskForm.controls['training'].value;
    this.taskService.viewTraining(trainingId).subscribe({
      next: (resp: any) => {
        this.training = resp.trainingId;
        let date1=formatDate(new Date(this.minDate),'yyyy-MM-dd','en_US');
      let date2=formatDate(new Date(resp.trainingStartDate),'yyyy-MM-dd','en_US')
      // if((new Date(this.minDate).getFullYear())<=(new Date(resp.trainingStartDate).getFullYear())&&(new Date(this.minDate).getMonth())<=(new Date(resp.trainingStartDate).getMonth())&&(new Date(this.minDate).getDate())<=(new Date(resp.trainingStartDate).getDate()))
      if(date1<=date2)
      {
        console.log("inside if");
        
          this.minDate = new Date(resp.trainingStartDate);
          this.TaskForm.controls['taskStart'].setValue(resp.trainingStartDate);
          this.TaskForm.controls['taskEnd'].setValue(resp.trainingStartDate);
        } else {
          console.log("inside else");
          this.TaskForm.controls['taskStart'].setValue(this.minDate);
          this.TaskForm.controls['taskEnd'].setValue(this.minDate);
        }
      },
      error: (err: any) => {
        if (err.error.errorCode == '1912') {
          this.toastrService.error('Training not found', '');
        }else if(err.error.errorCode == '1931'){
          this.toastrService.error('Training should be of a positive integer', '');

        } else {
          this.toastrService.error(
            'User has no permission to access this action',
            ''
          );
        }
      },
    });
  }


  updateTask() {
    this.submitted = true;
    console.log('form', this.TaskForm);

    if (this.TaskForm.invalid) {
      this.TaskForm.markAllAsTouched();
      this.submitted = false;
    } else {
      let taskStartTime = new Date(
        this.TaskForm.controls['taskStart'].value
      );
      let taskEndTime = new Date(this.TaskForm.controls['taskEnd'].value);

      let param = {
        trainingId: this.TaskForm.value.training,
        taskName: this.TaskForm.value.taskName,
        taskDescription: this.TaskForm.value.taskDescription,
        taskStart: this.datepipe.transform(
          new Date(taskStartTime),
          'yyyy-MM-dd'
        ),
        taskEnd: this.datepipe.transform(
          new Date(taskEndTime),
          'yyyy-MM-dd'
        ),
      };
      console.log(param, 'params');
      console.log(this.taskId, 'kokokokokokokok');

      this.submitted = true;
      this.taskService.editTask(param, this.taskId).subscribe({
        next: (resp: any) => {
          this.submitted = true;
          this.activeModal.close();

          this.toastrService.success('Task details updated', '');
        },
        error: (err: any) => {
          this.submitted = false;
          console.log(err,"err");
          
          const errorMessage = this.errorMessages[err.error.errorCode];
          this.toastrService.error(errorMessage, '');
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }
}
