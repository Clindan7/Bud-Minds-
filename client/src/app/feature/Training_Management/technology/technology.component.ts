import { LocationStrategy } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TrainingServiceService } from 'src/app/core/services/training-service.service';
import { TechnologyListComponent } from '../technology-list/technology-list.component';

@Component({
  selector: 'app-technology',
  templateUrl: './technology.component.html',
  styleUrls: ['./technology.component.css']
})
export class TechnologyComponent implements OnInit {

  joinerBatch: any;
  batches: any;
  id: any;
  minDate: Date;
  maxDate: Date;
  currentData: Date;
  len: any;
  modelDate = '';
  page: any = 1;
  limit: any = 10;
  searchBatchName: any;
  updateMode: boolean = false;
  submitted: boolean = false;
  modalRef:any;
  getBrowserbutton:any;
  technology: any;
  currentYear:any=new Date().getFullYear();
  isSuccess:boolean=false;
  // @Output() isSuccess: EventEmitter<any> = new EventEmitter<any>();

  technologyForm:FormGroup = new FormGroup({
    technologyName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-z0-9_@./#&+:;~`!#$%^&/+*()={}?<>,\'"|-]+([\\s][a-zA-Z0-9_@.:;~`!#$%^&/+*()={}?<>,\'"|-]+)*$'),
      Validators.maxLength(50),
    ])

  });


  constructor(public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private trainingService: TrainingServiceService,
    private toast: ToastrService,
    private fb: FormBuilder,
    private location: LocationStrategy) { }

  ngOnInit(): void {
  }

  validateForm() {
    if (this.technologyForm.invalid) {
      this.technologyForm.markAllAsTouched();
      this.submitted = false;
      return false;
    }
    return true;
  }

  close(){
    this.modalRef = this.modalService.dismissAll();
  }

  createTechnology() {
    if (!this.validateForm()) {
      return;
    }
    let param = {
        technologyName: this.technologyForm.value.technologyName,
      };
      this.submitted = true;
      this.trainingService.addTechnology(param).subscribe({
        next: (resp: any) => {
          this.toast.success('Technology Added Successfully', '');
          this.isSuccess=true;
          this.activeModal.close();
        },
        error: (err: any) => {
          this.submitted = false;
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          } else if (err.error.errorCode == '1957') {
            this.toast.error('Technology Name is required', '');
          } else if (err.error.errorCode == '1958') {
            this.toast.error('Technology Name size must be between 1 and 50', '');
          } else if (err.error.errorCode == '1959') {
            this.toast.error('Invalid technology name', '');
          } else if (err.error.errorCode == '1953') {
            this.toast.error('Technology name already exists', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        }
      });
    }
}
