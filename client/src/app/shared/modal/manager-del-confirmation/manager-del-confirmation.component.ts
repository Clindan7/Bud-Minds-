import { LocationStrategy } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manager-del-confirmation',
  template: `
  <div class="modal-header">
    <h4 class="modal-title">{{ title }}</h4>
    <button
    type="button"
    class="close waves-effect waves-light"
    data-dismiss="modal"
    aria-label="Close"
    (click)="activeModal.close('cancel')"
  >
    <span aria-hidden="true">&times;</span>
  </button>
  </div>
  <div class="modal-body">{{ message }}</div>
  <div class="modal-footer">
  <button
  type="button"
  class="btn btn-light waves-effect  waves-light"
  data-dismiss="modal"
  (click)="activeModal.close()"
>
  Cancel
</button>
    <button type="button" class="btn btn-danger" (click)="nav()">Deallocate</button>
  </div>
`
})
export class ManagerDelConfirmationComponent  {

  @Input() title: string;
  @Input() message: string;
  @Input() btn:string;
  @Input() managerId:number;

  constructor(public activeModal: NgbActiveModal,
    private location: LocationStrategy,private router:Router) {
      if(this.activeModal){
        history.pushState(null, null, window.location.href);
        this.location.onPopState(() => {
        window.onpopstate = function (e) { window.history.forward(); }
          this.activeModal.dismiss()
        });
        window.onpopstate = function (e) { window.history.back(); }
        }
  }

  nav(){
    this.activeModal.close('confirm');
    this.router.navigateByUrl("/manager_resource_allocation/" + this.managerId)
  }
}
