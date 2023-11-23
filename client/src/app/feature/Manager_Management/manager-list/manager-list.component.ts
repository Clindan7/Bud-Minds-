import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manager-list',
  templateUrl: './manager-list.component.html',
  styleUrls: ['./manager-list.component.css']
})
export class ManagerListComponent implements OnInit {

  page: number = 1;
  closeResult = '';
  modalOpen : boolean = false;
  confirmModalOpen : boolean = false;
  confirmClick : boolean = true;

  @Input() searchResult: any;
  @Output() userData: EventEmitter<any> = new EventEmitter<any>();


  constructor(private modalService: NgbModal) { }

  userrole=1
  searchedUserData: any;

  ngOnInit(): void { /* 'ngOnInit' is empty */ }

  handleSearchResult(searchResult: any) {


    this.searchedUserData = searchResult;

    this.userData.emit(this.searchedUserData);


  }
}
