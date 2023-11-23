import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from 'src/app/core/services/user-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user: any;
  @Output() buddySubmit = new EventEmitter<any>();
  toggle : boolean = false;

  constructor(private userService: UserServiceService,
    private router: Router,
    private toast: ToastrService) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  onHandleToggle(): void{
    this.toggle = !this.toggle;
    this.buddySubmit.emit(this.toggle);
  }

  logout()
  {
    localStorage.clear();
    this.router.navigateByUrl("/login")
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
