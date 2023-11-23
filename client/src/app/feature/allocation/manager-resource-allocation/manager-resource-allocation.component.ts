import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-manager-resource-allocation',
  templateUrl: './manager-resource-allocation.component.html',
  styleUrls: ['./manager-resource-allocation.component.css'],
})
export class ManagerResourceAllocationComponent implements OnInit {
  constructor() {
     /* this constructor is empty */
    }

  active: number = 1;

  ngOnInit(): void {
    /* 'ngOnInit' is empty */
  }
}
