import { LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarCellViewModel } from 'ngx-bootstrap/datepicker/models';
import { ToastrService } from 'ngx-toastr';
import { BatchService } from 'src/app/core/services/batch.service';

@Component({
  selector: 'app-joiner-batch',
  templateUrl: './joiner-batch.component.html',
  styleUrls: ['./joiner-batch.component.css']
})
export class JoinerBatchComponent implements OnInit {

  joinerBatch: any;
  batches: any;
  id: any;
  minDate: Date;
  maxDate: Date;
  currentData: Date;
  len: any;
  batchData: any;
  groupData: any;
  modelDate = '';
  page: any = 1;
  limit: any = 10;
  searchBatchName: any;
  updateMode: boolean = false;
  submitted: boolean = false;
  getBrowserbutton:any;
  currentYear:any=new Date().getFullYear();



  @Input() batchId: number;

  batchForm : FormGroup = this.fb.group({
    batchName: new FormControl('', [
      Validators.pattern('^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$'),
      Validators.maxLength(10),
    ]),
      month: new FormControl(new Date().toLocaleString('default', { month: 'long' }), [
        Validators.required
    ]),
      year: new FormControl(new Date().getFullYear().toString(), [
      Validators.required
  ]),


  });

  constructor(private batchService: BatchService,
    public activeModal: NgbActiveModal,
    private toast: ToastrService,
    private fb: FormBuilder,
    private location: LocationStrategy)
   {

    if(this.activeModal){
      history.pushState(null, null, window.location.href);
      this.location.onPopState(() => {
      window.onpopstate = function (e) { window.history.forward(); }
        this.activeModal.dismiss()
      });
      window.onpopstate = function (e) { window.history.back(); }
      }
      this.minDate = new Date();
      this.currentData = new Date();
      this.currentData.setFullYear(this.minDate.getFullYear() -1);
      this.minDate.setDate(1);
      this.minDate.setMonth(12)
      this.minDate.setFullYear(this.minDate.getFullYear() -2);
     }

  ngOnInit(): void {
    if (this.batchId) {
      this.updateMode = true;
      this.getBatch();
    }
  }

  getBatch() {
    this.batchService.viewBatch(this.batchId).subscribe({
      next: (resp: any) => {
        this.joinerBatch = resp;
        this.setDefault();
      },
      error: (err: any) => {
        if (err.error.errorCode == '1093') {
          this.toast.error('Batch not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        } else {
          this.toast.error('User has no permission to access this action', '');
        }
      },
    });
  }


  setDefault() {
    let batchName = this.joinerBatch[0].joinerBatchName.split(" ",4);
    let batchname=batchName[2]
    if(batchName.length>3)
     batchname=batchname +" "+batchName[3];

    this.batchForm.patchValue({
      month: batchName[0],
      year: batchName[1],
      batchName: batchname
    });
  }
  listapi(){
    this.batchService
      .findBatches(this.page, this.limit)
      .subscribe((data: any) => {
        this.batchData = data.result;
      })
  }

  createBatch() {
    if (this.batchForm.invalid) {
      this.batchForm.markAllAsTouched();
      this.submitted = false;
    }
    else {
      let year=new Date(this.batchForm.value.year);
      let param = {
        joinerBatchName: this.batchForm.value.batchName,
        joinerBatchMonth: this.batchForm.value.month,
        joinerBatchYear: year.getFullYear()
      };

      this.batchService.addBatch(param).subscribe({

        next: (resp: any) => {
          this.toast.success('Batch Added Successfully', '');
          this.submitted = true;
          this.activeModal.close();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          } else if (err.error.errorCode == '1966') {
            this.toast.error('Batch Name size must be between 1 and 30', '');
          } else if (err.error.errorCode == '1968') {
            this.toast.error('Invalid Batch name', '');
          } else if (err.error.errorCode == '1976') {
            this.toast.error('Year is Required', '');
          } else if (err.error.errorCode == '1967') {
            this.toast.error(
              'This batch name already exists, try another name.',
              ''
            );
          } else if (err.error.errorCode == '1979') {
            this.toast.error('Invalid batch month format, must be alphabets', '');
          } else if (err.error.errorCode == '1977') {
            this.toast.error('Batch month required', '');
          } else if (err.error.errorCode == '1975') {
            this.toast.error('This year is not valid', '');
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        },
        complete: () => {
          this.submitted = false;
        },
      });
    }
  }

  searchBatch() {
    this.batchService
      .findBatches(this.page, this.limit)
      .subscribe({
        next: (res: any) => {
          this.len = res.numItems;
          this.batchData = res.result;

        },
      });
  }

  updateBatch() {
    if (this.batchForm.invalid) {
      this.batchForm.markAllAsTouched();
      this.submitted = false;
    }
    else {
      let date = new Date(this.batchForm.value.year);
      let year = date.getFullYear();
      let param = {
        joinerBatchName: this.batchForm.value.batchName,
        joinerBatchMonth: this.batchForm.value.month,
        joinerBatchYear: year
      };
      this.batchService.editBatch(param, this.batchId).subscribe({
        next: (resp: any) => {
          this.toast.success('Batch Updated Successfully', '');
          this.submitted = true;
          this.activeModal.close();
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
              ''
            );
          } else if (err.error.errorCode == '1969') {
            this.toast.error('Batch is not found', '');
          } else if (err.error.errorCode == '1966') {
            this.toast.error('Batch Name size must be between 1 and 30', '');
          } else if (err.error.errorCode == '1968') {
            this.toast.error('Invalid Batch Name', '');
          } else if (err.error.errorCode == '1976') {
            this.toast.error('Year is required', '');
          } else if (err.error.errorCode == '1967') {
            this.toast.error(
              'This batch name already exists, try another name.',
              ''
            );
          } else if (err.error.errorCode == '1979') {
            this.toast.error('Invalid batch month format, must be alphabets', '');
          } else if (err.error.errorCode == '1977') {
            this.toast.error('Batch month required', '');
          } else if (err.error.errorCode == '1975') {
            this.toast.error('This year is not valid', '');
          }
        },
        complete: () => {
          this.submitted = false;
        },
      });

    }
  }

  onOpenCalendarYear(container) {
    container.setViewMode('year');

    container.yearSelectHandler = (event: CalendarCellViewModel): void => {
      container.value = event.date;
    };
  }
}

