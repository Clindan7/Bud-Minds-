import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-mentor-list',
  templateUrl: './mentor-list.component.html',
  styleUrls: ['./mentor-list.component.css']
})
export class MentorListComponent implements OnInit {

  page: number = 1;
  closeResult = '';
  modalOpen : boolean = false;
  confirmModalOpen : boolean = false;
  confirmClick : boolean = true;

  constructor(private modalService: NgbModal) { }

  userrole=2
  searchedUserData: any;

  ngOnInit(): void {}

  handleSearchResult(searchResult: any) {
    this.searchedUserData = searchResult;
  }

}
