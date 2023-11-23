import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-trainer-list',
  templateUrl: './trainer-list.component.html',
  styleUrls: ['./trainer-list.component.css']
})
export class TrainerListComponent implements OnInit {

  page: number = 1;
  closeResult = '';
  modalOpen : boolean = false;
  confirmModalOpen : boolean = false;
  confirmClick : boolean = true;

  constructor(private modalService: NgbModal) { }

  userrole=3
  searchedUserData: any;

  ngOnInit(): void {}

  handleSearchResult(searchResult: any) {
    this.searchedUserData = searchResult;
  }

}
