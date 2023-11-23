import { DatePipe, LocationStrategy, Time, formatDate } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal, NgbActiveModal, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TrainingServiceService } from 'src/app/core/services/training-service.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TechnologyListComponent } from '../../Training_Management/technology-list/technology-list.component';
import { SessionServiceService } from 'src/app/core/services/session-service.service';

@Component({
  selector: 'app-session-form',
  templateUrl: './session-form.component.html',
  styleUrls: ['./session-form.component.css']
})
export class SessionFormComponent implements OnInit {

  updateMode: boolean = false;
  submitted: boolean = false;
  minDate: any;
  maxDate: Date;
  technologies:any;
  session:any
  trainings:any;
  trainers:any;
  groups:any;
  user: any;
  technologyData: any;
  sessionStart:any;
  sessionEnd:any;
  modalRef:any;
  confirmModalOpen : boolean = false;
  isNewTech:boolean=false;
  totalTech:any;
  temp:any;
  scope:any;
  index:number;
  date: any;
  time: { hour: number, minute: number};
  endtime: { hour: number, minute: number};
  _value;
  label;
  _value1;
  label1;

  @Input() sessionId: number;
  @Input() isAdded: boolean=false;


  sessionForm = this.fb.group({
    training: new FormControl('', [
      Validators.required
    ]),
    description: new FormControl('', [
      // Validators.pattern('^[a-zA-z0-9_@./#&+:;~`!#$%^&/+*()={}?<>,\'"|-]+([\\s][a-zA-Z0-9_@.:;~`!#$%^&/+*()={}?<>,\'"|-]+)*$'),
      // '^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$'
      Validators.pattern('^[^\\s]+([\\s][^\\s]+)*$'),
      Validators.maxLength(250),
    ]),
    sessionStart: new FormControl('', [
      Validators.required
    ]),
    sessionEnd: new FormControl('', [
      Validators.required
    ]),
    trainer: new FormControl('', [
      Validators.required
    ]),
    group: new FormControl('', [
      Validators.required
    ]),
    time: new FormControl(''),
    endtime: new FormControl('')
  })
  training: any;

  constructor(private router: Router,
    private sessionService: SessionServiceService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private datepipe: DatePipe,
    private location: LocationStrategy
  ) { }

  ngOnInit(): void {
    this.getDatetime();  
    this.getDateendtime();
    this.minDate = new Date();
    this.sessionStart = new Date();
    this.sessionEnd = new Date();
    this.sessionForm.patchValue({
    
      description: "",
      sessionStart: this.sessionStart,
      sessionEnd:this.sessionEnd
    });

    this.getTraining();
    this.getTrainer();
    this.getGroup();
    if (this.updateMode) {
      this.getTraining();
      this.getTrainer();
      this.getGroup();
      this.getSession();
    }

  }

  getTraining() {
    this.sessionService.getUpcomingTraining().subscribe({
      next: (resp: any) => {
        this.trainings = resp;
        console.log(this.trainings,"FOR TEST");
        
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toastrService.error('Trainings not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
      },
    });
  }

  getSession() {
    this.sessionService.viewSession(this.sessionId).subscribe({
      next: (resp: any) => {
        this.session = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toastrService.error('Session not found', '');
        } else {
          this.toastrService.error('User has no permission to access this action', '');
        }
      },
    });
  }

  setDefault() {
    
    let sessionTime=this.session.sessionStart.split("T")
    let hourAndMinute=sessionTime[1].split(":")
    console.log(hourAndMinute[1]);
    
    this.time= { hour:Number(hourAndMinute[0]), minute: Number(hourAndMinute[1])};

    let sessionEndTime=this.session.sessionEnd.split("T")
    let endhourAndMinute=sessionEndTime[1].split(":")
    console.log(endhourAndMinute[1]);

    this.endtime= { hour:Number(endhourAndMinute[0]), minute: Number(endhourAndMinute[1])};
    

    console.log(this.session,"session");
    console.log(this.session.description);
    console.log(this.session.trainingId);

    this.sessionForm.patchValue({
      training: this.session.trainingId,
      description: this.session.description,
      sessionStart: this.session.sessionStart,
      sessionEnd: this.session.sessionEnd,
      trainer: this.session.trainerId,
      group: this.session.joinerGroup,
    });

    let date1 = formatDate(new Date(this.minDate), 'yyyy-MM-dd', 'en_US');
    let date2 = formatDate(new Date(this.session.sessionStart), 'yyyy-MM-dd', 'en_US');
  
    if (date1 <= date2) {
      console.log("inside if");
      this.minDate = new Date(this.session.sessionStart);
      this.sessionForm.patchValue({
        sessionStart: this.session.sessionStart,
        sessionEnd: this.session.sessionStart,
      });
    } else {
      console.log("inside else");
      this.sessionForm.patchValue({
        sessionStart: this.minDate,
        sessionEnd: this.minDate,
      });
    }

    console.log(this.sessionForm);
    
  }

  onInputFocus(event: any) {
    event.target.classList.add('selected-input');
  }

  onInputBlur(event: any) {
    event.target.classList.remove('selected-input');
  }

  getTrainer() {
    this.sessionService.getTrainerList().subscribe({
      next: (resp: any) => {
        this.trainers = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toastrService.error('Trainer not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
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
          this.toastrService.error('Groups not found', '');
        } else if (err.error.errorCode == "1930") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
      },
    });
  }

  errorMessages = {
    "1912": "Training not Found",
    "1093": "User has no permission to access this action",
    "1925": "Description size must be between 1 and 250",
    "1905": "Training is required",
    "1978": "Trainer is required",
    "1986": "Trainer already assigned",
    "1934": "Trainer not found",
    "1962": "GroupId is required",
    "1956": "Group already assigned",
    "1960": "Group not found",
    "1917": "End date is required",
    "1916": "Start date is required",
    "1948": "Start date is invalid",
    "1945":"End date is invalid",
    "1927": "Session start date invalid",
    "1961": "No users found in the group",
    "1908": "Invalid token",
    "1909": "Authorization token expired",
  };

  createSession() {
    if (this.sessionForm.invalid) {
      this.sessionForm.markAllAsTouched();
      this.submitted = false;
    } else {
    let sessionStartTime=new Date(this.sessionForm.controls['sessionStart'].value);
    sessionStartTime.setHours(this.time.hour)
    sessionStartTime.setMinutes(this.time.minute)
    let sessionEndTime=new Date(this.sessionForm.controls['sessionEnd'].value);
    sessionEndTime.setHours(this.endtime.hour)
    sessionEndTime.setMinutes(this.endtime.minute)
    let param = {
      trainingId: this.sessionForm.value.training,
      description: this.sessionForm.value.description,
      sessionStart:this.datepipe.transform(sessionStartTime, 'yyyy-MM-ddTHH:mm:ss'),
      sessionEnd:this.datepipe.transform(sessionEndTime, 'yyyy-MM-ddTHH:mm:ss'),
      trainerId: this.sessionForm.value.trainer,
      groupId: this.sessionForm.value.group
    };
    this.submitted = true;
    this.sessionService.addSession(param).subscribe({

      next: (resp: any) => {
        this.toastrService.success("Added Session", "");
        this.activeModal.close();
      },
      error: (err: any) => {
        this.submitted = false;
        const errorMessage =
          this.errorMessages[err.error.errorCode];
      
        this.toastrService.error(errorMessage, "");
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }
}


updateSession(){
  this.submitted = true;
console.log("form",this.sessionForm);

  if (this.sessionForm.invalid) {
    this.sessionForm.markAllAsTouched();
    this.submitted = false;
  } else {
    let sessionStartTime=new Date(this.sessionForm.controls['sessionStart'].value);
    sessionStartTime.setHours(this.time.hour)
    sessionStartTime.setMinutes(this.time.minute)
    let sessionEndTime=new Date(this.sessionForm.controls['sessionEnd'].value);
    sessionEndTime.setHours(this.endtime.hour)
    sessionEndTime.setMinutes(this.endtime.minute)
    
  let param = {
    trainingId: this.sessionForm.value.training,
      description: this.sessionForm.value.description,
      sessionStart:this.datepipe.transform(new Date(sessionStartTime), 'yyyy-MM-ddTHH:mm:ss'),
      sessionEnd:this.datepipe.transform(new Date(sessionEndTime), 'yyyy-MM-ddTHH:mm:ss'),
      trainerId: this.sessionForm.value.trainer,
      groupId: this.sessionForm.value.group
    }

    this.submitted=true;
  this.sessionService.editSession(param,this.sessionId).subscribe({
    next: (resp: any) => {
        this.submitted = true;
        this.activeModal.close();
    
      this.toastrService.success('Session details updated', '');
    },
    error: (err: any) => {
      this.submitted = false;
      const errorMessage =
          this.errorMessages[err.error.errorCode];
        this.toastrService.error(errorMessage, "");
    },
    complete: () => {
      this.submitted = false;
    }
  });
}
}

start(){
  if(this.sessionStart>this.sessionEnd){
  this.sessionForm.patchValue({
    sessionEnd:this.sessionStart
  });
}
}

close(){
  this.modalRef = this.modalService.dismissAll();
}

getDatetime() {
  let value = null;
  if (!this.date) {
    if (!this.time) {
      value =  new Date().getDate()+'/'+new Date().getMonth()+'/'+new Date().getFullYear()+' '+ new Date().getHours() +':'+ new Date().getMinutes();
      this.time = {
        hour: new Date().getHours(),
        minute: new Date().getMinutes()
      };
    }
    else
      value =
         new Date().getDate()+'/'+new Date().getMonth()+'/'+new Date().getFullYear()+' '+ new Date().getHours() +':'+ new Date().getMinutes();;
  }
  if (!value) {
    value = new Date(
      this.date.year,
      this.date.month - 1,
      this.date.day,
      this.time ? this.time.hour : 0,
      this.time ? this.time.minute : 0
    );
    this._value=value;
   
  } else 
    this._value=null
  this.sessionForm.get("time").setValue(this._value);
  this.label=value;
 
}

getDateendtime() {
  let value1 = null;
  if (!this.date) {
    if (!this.endtime) {
      value1 =  new Date().getDate()+'/'+new Date().getMonth()+'/'+new Date().getFullYear()+' '+ new Date().getHours() +':'+ new Date().getMinutes();
      this.endtime = {
        hour: new Date().getHours(),
        minute: new Date().getMinutes()
      };
    }
    else
      value1 =
         new Date().getDate()+'/'+new Date().getMonth()+'/'+new Date().getFullYear()+' '+ new Date().getHours() +':'+ new Date().getMinutes();;
  }
  if (!value1) {
    value1 = new Date(
      this.date.year,
      this.date.month - 1,
      this.date.day,
      this.endtime ? this.endtime.hour : 0,
      this.endtime ? this.endtime.minute : 0
    );
    this._value1=value1;
    console.log(value1)
  } else 
    this._value1=null
  this.sessionForm.get("endtime").setValue(this._value1);
  this.label1=value1;
  console.log(this._value1)
}

getStartandEndDate(){
  this.minDate=new Date();
  let trainingId=this.sessionForm.controls['training'].value;
  this.sessionService.viewTraining(trainingId).subscribe({
    next: (resp: any) => {
      this.training = resp.trainingId;
      console.log("date", resp.trainingStartDate);
      console.log("ddd",new Date(this.minDate).getMonth()+1);
      
      console.log("start",new Date(resp.trainingStartDate).getMonth()+1);
      let date1=formatDate(new Date(this.minDate),'yyyy-MM-dd','en_US');
      let date2=formatDate(new Date(resp.trainingStartDate),'yyyy-MM-dd','en_US')
      // if((new Date(this.minDate).getFullYear())<=(new Date(resp.trainingStartDate).getFullYear())&&(new Date(this.minDate).getMonth())<=(new Date(resp.trainingStartDate).getMonth())&&(new Date(this.minDate).getDate())<=(new Date(resp.trainingStartDate).getDate()))
      if(date1<=date2)
      { 
        console.log("dae", resp.trainingStartDate);
        
      this.minDate=new Date(resp.trainingStartDate)
      this.sessionForm.controls['sessionStart'].setValue(resp.trainingStartDate);
      this.maxDate=new Date(resp.trainingEndDate)
      this.sessionForm.controls['sessionEnd'].setValue(resp.trainingEndDate);
      }
      else {
      //   this.minDate=new Date(resp.trainingStartDate)
      // this.sessionForm.controls['sessionStart'].setValue(resp.trainingStartDate);
        this.maxDate=new Date(resp.trainingEndDate)
      this.sessionForm.controls['sessionEnd'].setValue(resp.trainingEndDate);
      }
    },
    error: (err: any) => {
      if (err.error.errorCode == '1093') {
        this.toastrService.error('Session not found', '');
      } else {
        this.toastrService.error('User has no permission to access this action', '');
      }
    },
  });
}

checkEndDateBefore(trainingEndDate:any){
  if((new Date(trainingEndDate).getFullYear())<=(new Date(this.minDate).getFullYear())&&(new Date(trainingEndDate).getMonth())<=(new Date(this.minDate).getMonth())&&(new Date(trainingEndDate).getDate())<(new Date(this.minDate).getDate()))
  {
    return true;
  }
  return false;
}
}
