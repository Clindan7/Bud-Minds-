import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-trainee-list',
  templateUrl: './trainee-list.component.html',
  styleUrls: ['./trainee-list.component.css']
})
export class TraineeListComponent implements OnInit {

  closeResult = '';
  modalOpen : boolean = false;
  confirmModalOpen : boolean = false;
  confirmClick : boolean = true;

  constructor(private modalService: NgbModal) { }

  userrole=4
  searchedUserData: any;

  ngOnInit(): void {}

  handleSearchResult(searchResult: any) {
    this.searchedUserData = searchResult;
  }

}
