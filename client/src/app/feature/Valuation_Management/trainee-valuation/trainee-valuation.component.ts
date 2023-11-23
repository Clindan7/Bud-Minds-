import { DatePipe, LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { SessionServiceService } from 'src/app/core/services/session-service.service';

@Component({
  selector: 'app-trainee-valuation',
  templateUrl: './trainee-valuation.component.html',
  styleUrls: ['./trainee-valuation.component.css']
})
export class TraineeValuationComponent implements OnInit {

  updateMode: boolean = false;
  submitted: boolean = false;
  modalRef:any;

  @Input() sessionId: number;

  constructor(private router: Router,
    private sessionService: SessionServiceService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private datepipe: DatePipe,
    private location: LocationStrategy) { }

  ngOnInit(): void {
  }

  valuationForm = this.fb.group({
    qualityOfWork: new FormControl('', [
      Validators.required
    ]),
    extraEffort: new FormControl('', [
      Validators.pattern('^[^\\s]+([\\s][^\\s]+)*$'),
      Validators.maxLength(250),
    ]),
    enthusiamToLearn: new FormControl('', [
      Validators.required
    ]),
    adaptability: new FormControl('', [
      Validators.required
    ]),
    teamWork: new FormControl('', [
      Validators.required
    ]),
    comment: new FormControl('', [
      Validators.pattern('^[^\\s]+([\\s][^\\s]+)*$'),
      Validators.maxLength(2000),
    ]),
  })
  
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
    if (this.valuationForm.invalid) {
      this.valuationForm.markAllAsTouched();
      this.submitted = false;
    } else {
    // let sessionStartTime=new Date(this.sessionForm.controls['sessionStart'].value);
    // sessionStartTime.setHours(this.time.hour)
    // sessionStartTime.setMinutes(this.time.minute)
    // let sessionEndTime=new Date(this.sessionForm.controls['sessionEnd'].value);
    // sessionEndTime.setHours(this.endtime.hour)
    // sessionEndTime.setMinutes(this.endtime.minute)
    let param = {
      qualityOfWork: this.valuationForm.value.qualityOfWork,
      extraEffort: this.valuationForm.value.extraEffort,
      enthusiamToLearn:this.valuationForm.value.enthusiamToLearn,
      adaptability:this.valuationForm.value.adaptability,
      teamWork: this.valuationForm.value.teamWork,
      comment: this.valuationForm.value.comment
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
console.log("form",this.valuationForm);

  if (this.valuationForm.invalid) {
    this.valuationForm.markAllAsTouched();
    this.submitted = false;
  } else {
    // let sessionStartTime=new Date(this.sessionForm.controls['sessionStart'].value);
    // sessionStartTime.setHours(this.time.hour)
    // sessionStartTime.setMinutes(this.time.minute)
    // let sessionEndTime=new Date(this.sessionForm.controls['sessionEnd'].value);
    // sessionEndTime.setHours(this.endtime.hour)
    // sessionEndTime.setMinutes(this.endtime.minute)
    
  let param = {
    qualityOfWork: this.valuationForm.value.qualityOfWork,
      extraEffort: this.valuationForm.value.extraEffort,
      enthusiamToLearn:this.valuationForm.value.enthusiamToLearn,
      adaptability:this.valuationForm.value.adaptability,
      teamWork: this.valuationForm.value.teamWork,
      comment: this.valuationForm.value.comment
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

close(){
  this.modalRef = this.modalService.dismissAll();
}
}
