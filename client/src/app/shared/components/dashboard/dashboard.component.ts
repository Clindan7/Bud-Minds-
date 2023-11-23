import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from 'src/app/core/services/user-service.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private userService: UserServiceService,
    private router: Router,
    private toast: ToastrService) { }

    user: any;


  ngOnInit(): void {
    this.getCurrentUser();
  }
  logout()
  {
    localStorage.removeItem("accessToken")
    this.router.navigateByUrl("/login")
    window.location.reload()
  }
  getCurrentUser() {
    this.userService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
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
}
