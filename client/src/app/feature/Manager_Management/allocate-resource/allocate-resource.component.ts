import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from 'src/app/core/services/user-service.service';

@Component({
  selector: 'app-allocate-resource',
  templateUrl: './allocate-resource.component.html',
  styleUrls: ['./allocate-resource.component.css']
})
export class AllocateResourceComponent implements OnInit {

user:any;

  constructor(private userService: UserServiceService,
    private toast: ToastrService) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.userService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == 1912) {
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
