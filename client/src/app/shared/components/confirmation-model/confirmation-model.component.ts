import { LocationStrategy } from '@angular/common';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-model',
  templateUrl: './confirmation-model.html',
  styleUrls: ['./confirmation-model.component.css']
})
export class ConfirmationModelComponent {
  @Input() title: string;
  @Input() message: string;

  constructor(public activeModal: NgbActiveModal,
    private location: LocationStrategy) {
      if(this.activeModal){
        history.pushState(null, null, window.location.href);
        this.location.onPopState(() => {
        window.onpopstate = function (e) { window.history.forward(); }
          this.activeModal.dismiss()
        });
        window.onpopstate = function (e) { window.history.back(); }
        }
  }
}

