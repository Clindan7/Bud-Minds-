import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  toggle : boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  updateBuddy($event : any){
    this.toggle = $event;
  }
}
