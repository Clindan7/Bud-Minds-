import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from 'src/app/core/services/user-service.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  collapse : boolean = false;
  account1: boolean = false;
  option2: boolean = false;
  option3: boolean = false;
  option4: boolean = false;
  option5: boolean = false;
  user: any;

  constructor(private userService: UserServiceService,
    private router: Router,
    private toast: ToastrService) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  onHandleCollapse(): void{
    this.collapse = ! this.collapse;
  }

  getCurrentUser() {
    this.userService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == 1912) {
          this.toast.error('User not found', '');
        }
      },
    });
  }
}
