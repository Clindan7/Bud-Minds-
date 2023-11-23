import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LocationStrategy } from '@angular/common';
import { BatchService } from 'src/app/core/services/batch.service';

@Component({
  selector: 'app-view-modal',
  templateUrl: './view-modal.component.html',
  styleUrls: ['./view-modal.component.css']
})
export class ViewModalComponent implements OnInit {

  constructor(
    private batchservice :BatchService,
    private toast: ToastrService,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private location: LocationStrategy
    ) {
      if(this.activeModal){
        history.pushState(null, null, window.location.href);
        this.location.onPopState(() => {
        window.onpopstate = function (e) { window.history.forward(); }
          this.activeModal.dismiss()
        });
        window.onpopstate = function (e) { window.history.back(); }
        }
     }
  batches:any;
  groups:any;
  i:any=1;
  groupLength:any;


  @Input() id:number;

  ngOnInit(): void {
    this.batchservice.viewBatch(this.id).subscribe({
      next: (resp: any) => {
        this.batches = resp[0];
        this.groups = this.batches.joinerGroup
        this.groupLength = this.batches.joinerGroup.length
      },
      error: (err: any) => {
        if (err.error.errorCode == "1093") {
          this.toast.error('User has no permission to access this action', '');
        }
        else if (err.error.errorCode == "1969") {
          this.toast.error('Batch not found', '');
        }
      },
    });
  }

  closeModal(){
    this.modalService.open(ViewModalComponent, {

    });
  }

}
