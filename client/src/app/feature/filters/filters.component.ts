import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ManagerServiceService } from 'src/app/core/services/manager-service.service';
import { MentorServiceService } from 'src/app/core/services/mentor-service.service';
import { TraineeServiceService } from 'src/app/core/services/trainee-service.service';
import { TrainerServiceService } from 'src/app/core/services/trainer-service.service';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.css']
})
export class FiltersComponent implements OnInit {

  user1: any;
  page:any=1;
  limit:any=10;
  len:any;
  userData: any;
  keyword = 'firstName';

  @Input() userrole: any;
  @Output() searchResult: EventEmitter<any> = new EventEmitter<any>();

  searchFirstName:FormGroup = new FormGroup({
    name: new FormControl(""),
    employeeId : new FormControl("")
  })

  constructor(private managerService: ManagerServiceService,
    public mentorService: MentorServiceService,
    public trainerService: TrainerServiceService,
    public traineeService: TraineeServiceService,
    private toast: ToastrService,
    private router: Router) { }

  ngOnInit(): void {
    let userService: any;

    switch (this.userrole) {
      case 1:
        userService = this.managerService;
        break;
      case 2:
        userService = this.mentorService;
        break;
      case 3:
        userService = this.trainerService;
        break;
      case 4:
        userService = this.traineeService;
        break;
      default:
        this.toast.error('Invalid user role');
        return;
    }

    userService.searchUser().subscribe({
      next: (resp: any) => {
        if (resp) {
          this.user1 = resp;
        }
      },
      error: (err: any) => {
        if (err.error.errorCode == "1912") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });

  }

  clearSearchTerm(): void {
    this.searchFirstName.controls['employeeId'].reset();
    this.searchFirstName.value.employeeId="";
  }

  omit_special_char(event)
  {
    let k;
    k = event.charCode;
    return((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8 || (k >= 48 && k <= 57));
  }

  search(searchMgr?:any){

    this.searchResult.emit(this.searchFirstName.value);
}
}
