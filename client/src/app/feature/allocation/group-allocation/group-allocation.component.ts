import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-group-allocation',
  templateUrl: './group-allocation.component.html',
  styleUrls: ['./group-allocation.component.css']
})
export class GroupAllocationComponent implements OnInit {

  constructor() {
    /* this constructor is empty */
   }

  active:number = 1;

  ngOnInit(): void {
    // 'ngOnInit' is empty
  }

}
