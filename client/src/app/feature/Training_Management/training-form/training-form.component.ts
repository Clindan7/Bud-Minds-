import { DatePipe, LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ToastrService } from 'ngx-toastr';
import { TrainingServiceService } from 'src/app/core/services/training-service.service';
import { ConfirmationModelComponent } from 'src/app/shared/components/confirmation-model/confirmation-model.component';
import { TechnologyListComponent } from '../technology-list/technology-list.component';
import { TechnologyComponent } from '../technology/technology.component';


@Component({
  selector: 'app-training-form',
  templateUrl: './training-form.component.html',
  styleUrls: ['./training-form.component.css']
})
export class TrainingFormComponent implements OnInit {

  updateMode: boolean = false;
  submitted: boolean = false;
  minDate: Date;
  technologies:any;
  training:any
  user: any;
  technologyData: any;
  trainingStartDate:any;
  trainingEndDate:any;
  modalRef:any;
  confirmModalOpen : boolean = false;
  isNewTech:boolean=false;
  totalTech:any;
  temp:any;
  scope:any;
  index:number;
  model: NgbDateStruct;

  @Input() trainingId: number;
  @Input() isAdded: boolean=false;


  trainingForm = this.fb.group({
    title: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-z0-9_@./#&+:;~`!#$%^&/+*()={}?<>,\'"|-]+([\\s][a-zA-Z0-9_@.:;~`!#$%^&/+*()={}?<>,\'"|-]+)*$'),
      Validators.maxLength(50),
    ]),
    trainingDescription: new FormControl('', [
      Validators.pattern('^[^\\s]+([\\s][^\\s]+)*$'),
      Validators.maxLength(250),
    ]),
    trainingStartDate: new FormControl('', [
      Validators.required
    ]),
    trainingEndDate: new FormControl('', [
      Validators.required
    ]),
    technology: new FormControl('', [
      Validators.required
    ]),
    department: new FormControl('', [
      Validators.required
    ]),
  })

  constructor(private router: Router,
    private trainingService: TrainingServiceService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private datepipe: DatePipe,
    private location: LocationStrategy
  ) { }

  ngOnInit(): void {

    this.minDate = new Date();
    this.trainingStartDate = new Date();
    this.trainingEndDate = new Date();
    this.trainingForm.patchValue({
      title: "",
      trainingDescription: "",
      trainingStartDate: this.trainingStartDate,
      trainingEndDate:this.trainingEndDate
    });

    // document.querySelector('modal-container').addEventListener('scroll', function() {});
    this.getTechnology();
    if (this.updateMode) {
      this.getTraining();
    }
    this.getCurrentUser();
    this.getTechnologyList();

  }

  getCurrentUser() {
    this.trainingService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == "1912") {
          this.toastrService.error('User not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
      },
    });
  }

  getTechnologyList(){

    this.trainingService.getTechnology().subscribe({
      next: (res:any)=>{
      this.technologyData = res;
      },
      error: (err:any) => {
        if (err.error.errorCode == "1912"){
          this.toastrService.error('Training not found', '');
        } else if (err.error.errorCode == "1934"){
          this.toastrService.error('Invalid Department', '');
        } else if (err.error.errorCode == "1908") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        } else if(err.error.errorCode == "1931"){
          this.toastrService.error('Department id should be a positive integer', '');
        }
        this.technologyData=[];
      }
    })
  }

  getTechnology() {
    this.trainingService.getTechnology().subscribe({
      next: (resp: any) => {
        this.technologies = resp;
        this.totalTech=resp.length;
      if(this?.temp<this.totalTech){
        let i:any=0;
        let arr:any[]=[];
        for(i=0;i<this.technologies.length-1;i++){
          arr.push(this.technologies[i].technologyId);
        }
        const max: number = Math.max(...arr);
        this.index=arr.indexOf(max);
        this.trainingForm.patchValue({
          technology: this.technologies[this.index]?.technologyId,
        });
      }
      },
      error: (err: any) => {
        if (err.error.errorCode == "1908") {
          this.toastrService.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toastrService.error('Authorization token expired', '');
        }
        this.technologies=[];
      },
    });
  }

  getTraining() {
    this.trainingService.viewTraining(this.trainingId).subscribe({
      next: (resp: any) => {
        this.training = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toastrService.error('Group not found', '');
        } else {
          this.toastrService.error('User has no permission to access this action', '');
        }
      },
    });
  }

  setDefault() {
    this.trainingForm.patchValue({
      title: this.training.title,
      trainingDescription: this.training.trainingDescription,
      trainingStartDate: this.training.trainingStartDate,
      trainingEndDate: this.training.trainingEndDate,
      technology: this.training.technologyId.technologyId,
      department: this.training.departmentId
    });
  }

  onInputFocus(event: any) {
    event.target.classList.add('selected-input');
  }

  onInputBlur(event: any) {
    event.target.classList.remove('selected-input');
  }

  errorMessages = {
    "1912": "User not Found",
    "1093": "User has no permission to access this action",
    "1920": "Title is required",
    "1921": "Title size must be between 1 and 50",
    "1923": "Invalid title",
    "1925": "Description size must be between 1 and 250",
    "1917": "End date is required",
    "1916": "Start Date is required",
    "1965": "Technology is required",
    "1930": "Invalid JSON",
    "1934": "Invalid department",
    "1967": "Technology not found",
    "1945": "End date is invalid",
    "1948": "Start date is invalid",
    "1908": "Invalid token",
    "1909": "Authorization token expired",
  };

  createTraining() {
    if (this.trainingForm.invalid) {
      this.trainingForm.markAllAsTouched();
      this.submitted = false;
    } else {
    let param = {
      title: this.trainingForm.value.title,
      trainingDescription: this.trainingForm.value.trainingDescription,
      trainingStartDate:this.datepipe.transform(this.trainingForm.value.trainingStartDate, 'yyyy-MM-dd'),
      trainingEndDate:this.datepipe.transform(this.trainingForm.value.trainingEndDate, 'yyyy-MM-dd'),
      technologyId: this.trainingForm.value.technology,
      departmentId: this.trainingForm.value.department
    };
    this.submitted = true;
    this.trainingService.addTraining(param).subscribe({

      next: (resp: any) => {
        this.toastrService.success("Added Training", "");
        this.activeModal.close();
      },
      error: (err: any) => {
        this.submitted = false;
        const errorMessage =
          this.errorMessages[err.error[0].errorCode];
        this.toastrService.error(errorMessage, "");
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }
}


updateTraining(){
  this.submitted = true;

  if (this.trainingForm.invalid) {
    this.trainingForm.markAllAsTouched();
    this.submitted = false;
  } else {
  let param = {
      title:this.trainingForm.value.title,
      trainingDescription:this.trainingForm.value.trainingDescription,
      trainingStartDate:this.datepipe.transform(this.trainingForm.value.trainingStartDate, 'yyyy-MM-dd'),
      trainingEndDate:this.datepipe.transform(this.trainingForm.value.trainingEndDate, 'yyyy-MM-dd'),
      technologyId:this.trainingForm.value.technology,
      departmentId:this.trainingForm.value.department
    }

    this.submitted=true;
  this.trainingService.editTraining(param,this.trainingId).subscribe({
    next: (resp: any) => {
      if(resp){
        this.submitted = true;
        this.activeModal.close();
      }
      this.toastrService.success('Training details updated', '');
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
  if(this.trainingStartDate>this.trainingEndDate){
  this.trainingForm.patchValue({
    trainingEndDate:this.trainingStartDate
  });
}
}

launchConfirmModal(): void{
  this.confirmModalOpen = !this.confirmModalOpen;
}

openCreateModal() {
  const modal = this.modalService.open(TechnologyListComponent, {
    centered: true,
    backdrop: 'static'
  });
  modal.hidden.subscribe(() => {
    this.temp=this.totalTech;
    this.isNewTech=true;
    this.getTechnology();
  });
}

close(){
  this.modalRef = this.modalService.dismissAll();
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
          this.toastrService.success('Technology deleted successfully');
        },
        error: (err:any) => {
          if (err.error.errorCode == "1912") {
            this.toastrService.error('User not found', '');
          } else if (err.error.errorCode == "1908") {
            this.toastrService.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toastrService.error('Authorization token expired', '');
          } else if (err.error.errorCode == "1093"){
            this.toastrService.error('User has no permission to access this action', '');
          }
        },
        complete: () => {
          // this.modalService.open(TechnologyListComponent, {
          //   centered: true,
          // });
        },
      })
      }
      else{
        // this.modalService.open(TechnologyComponent, {
        //   centered: true,
        // });
      }
    }).catch((error) => {
    });
  }

}

}
