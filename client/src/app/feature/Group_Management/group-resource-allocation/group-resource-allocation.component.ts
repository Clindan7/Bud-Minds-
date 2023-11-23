import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GroupAllocationService } from 'src/app/core/services/group-allocation.service';
import { MentorResourceAllocationService } from 'src/app/core/services/mentor-resource-allocation.service';

@Component({
  selector: 'app-group-resource-allocation',
  templateUrl: './group-resource-allocation.component.html',
  styleUrls: ['./group-resource-allocation.component.css']
})
export class GroupResourceAllocationComponent implements OnInit {

  assigned:any;
  unassigned:any;
  groupId: any;
  page: any = 1;
  page2: any = 1;
  limit: any = 10;
  newArray: Array<number>[] = [];
  checked: any = 0;
  userRole: any = 4;
  len: any;
  lenn:any;
  users:any;
  mode:any=2;
  i:any;
  masterSelected:boolean
  selectallAssigned: boolean =false;
  selectAllElements: boolean =false;
  selectallUnassigned:boolean = false;
  selectElement: boolean =false;
  submitted: boolean =false;
  res:any;
  rest:any;

  constructor(private fb:FormBuilder,
    private groupAllocation: GroupAllocationService,
    private route: ActivatedRoute,
    private toast: ToastrService
    ) { 
      this.masterSelected = false;
    }

  searchUser: FormGroup = this.fb.group({
    searchAssigned: '',
    searchUnassigned:''
  });

  ngOnInit(): void {
    this.groupId = this.route.snapshot.params['id'];
    this.assignedTrainees(this.page,this.limit,this.groupId);
    this.totalTrainees(this.page,this.limit);
  }

  assignedTrainees(page: any, limit: any,mentorId:any,searchTrainee?:any) {
    this.newArray=[];
    this.selectallAssigned=false;
    this.selectAllElements=false;
    if(this.searchUser.value.searchAssigned!=''){
      page=1;
  }
    this.groupAllocation
      .assignedResources(page,limit,this.groupId,this.searchUser.value.searchAssigned)
      .subscribe({
        next: (resp: any) => {
          this.submitted = true;
          this.res=resp;
          this.len = resp.numItems;
          this.assigned = resp.result;
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } 
        },
        complete: () => {
          this.submitted = false;
          this.mode=2;          
        }
      });
  }

  totalTrainees(page: any, limit: any, searchMgr?: any) {
    this.newArray = [];
    this.selectallUnassigned=false;
    this.selectElement=false;
    if(this.searchUser.value.searchUnassigned!=''){
        page=1;
    }
    this.groupAllocation.unassignedResources(page,limit,this.searchUser.value.searchUnassigned).subscribe({
      next: (resp: any) => {
        this.submitted = true;
        this.lenn = resp.numItems;
        this.rest = resp;
        this.unassigned = resp.result;
      },
      error: (err: any) => {
        this.submitted = false;
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to access this action', '');
        } else if (err.error.errorCode == '1912') {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == '1996') {
          this.toast.error('Manager not found', '');
        } else if (err.error.errorCode == '1995') {
          this.toast.error('Invalid user role', '');
        }
      },
      complete: () => {
        this.submitted = false;
        this.mode=2;
      }
    });
  }

  allocateTrainees() {
    
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.groupAllocation
      .allocateResources(1, this.groupId, this.userRole, param)
      .subscribe({
        next: (resp: any) => {
          this.toast.success('Trainee sucessfully allocated to mentor', '');
          this.newArray.length = 0;
          // this.totalTrainees(this.page,this.limit);
        },
        error: (err: any) => {
          this.submitted=false;
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          }
        },
        complete: () => {
          
        },
      }).add(()=>
      {
        this.selectallUnassigned=false;
          this.selectElement=false;
          this.submitted = false;
          this.mode=null;
          if(this.rest.hasNext==true){
          this.assignedTrainees(this.page,this.limit,this.groupId);
          this.totalTrainees(this.page2,this.limit);
          }
          else{
            this.totalTrainees(this.page2-1,this.limit)
          this.assignedTrainees(this.page-1,this.limit,this.groupId);
          }
      });
  }

  deallocateTrainees() {
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.groupAllocation
      .deallocateResources(0, this.groupId, param)
      .subscribe({
        next: (resp: any) => { 
          this.toast.success('Trainee sucessfully deallocated from mentor', '');
          this.newArray.length = 0;
          // this.assignedTrainees(this.page,this.limit,this.groupId);
        },
        error: (err: any) => {
          this.submitted=false;
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } 
        },
        complete: () => {
          
        },
      }).add(()=>
      {        
        this.selectAllElements=false;
          this.selectallAssigned=false;
          this.submitted = false;
          if(this.res.hasNext==true)
          {
          this.assignedTrainees(this.page,this.limit,this.groupId);
          this.totalTrainees(this.page2,this.limit);}
          else{
          this.totalTrainees(this.page2-1,this.limit)
          this.assignedTrainees(this.page-1,this.limit,this.groupId);
          }
          this.mode=null;
      });
  }

  onCheckboxChange(e: any, data: any, mode: any) {   
    if (e.target.checked) {          
      this.newArray.push(data);
    } else {
      let removeIndex = this.newArray.findIndex((itm) => itm === data);
      if (removeIndex !== -1) 
      this.newArray.splice(removeIndex, 1);
    }
    if(mode==0){
      this.mode=0;
    }
    if(mode==1){
      this.mode=1;
    }
    if(this.newArray.length==0){
      this.mode=2;
    }
  }
  onPageChange($event,list) {   
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
    switch(list){
      case 1:
        this.page=$event;
        break;
      case 2:
        this.page2=$event;
        break;
    }
    this.assignedTrainees(this.page,this.limit,this.groupId);
    this.totalTrainees(this.page2,this.limit);
  }
  
  clearAssignedSearchTerm(): void {
    this.searchUser.controls['searchAssigned'].reset();
    this.searchUser.value.searchAssigned="";
  }

  clearUnassignedSearchTerm(): void {
    this.searchUser.controls['searchUnassigned'].reset();
    this.searchUser.value.searchUnassigned="";
  }

  selectAll(e:any,data:any){
    this.mode=0
    this.newArray=[];
    
    if(e.target.checked){
      this.selectallAssigned=true;
      this.selectAllElements=true;
      console.log(data);
      
      for(let i=0;i<data.length;i++){
      
      
      this.newArray.push(data[i].userId)
      }
    }
    else{
    this.selectallAssigned=false;
    this.selectAllElements=false;
    for(let i=0;i<data.length;i++){
      this.newArray.pop()
      }
      this.mode=null;
    }
  }

  selectAllUnassigned(e:any,data:any){
    this.mode=1
    this.newArray=[];
    
    if(e.target.checked){
      this.selectElement=true;
      this.selectallUnassigned=true;
      
      for(let i=0;i<data.length;i++){  
      this.newArray.push(data[i].userId)
      }
    }
    else{
    this.selectElement=false;
    this.selectallUnassigned=false;
    for(let i=0;i<data.length;i++){
      this.newArray.pop()
      }
      this.mode=null;
    }
  }

  checkUncheckAll(e: any, data: any) {
  
    if (e.target.checked) {
      for(let i=0;i<data.length;i++)
      this.newArray.push(data[i].userId);
    }
    else {
      let removeIndex = this.newArray.findIndex((itm) => itm === data);
      if (removeIndex !== -1) 
      this.newArray.splice(removeIndex, 1);
    }
    if (this.assigned.every(val => val.assigned == true))
      this.assigned.forEach(val => { val.assigned = false });
    else
      this.assigned.forEach(val => { val.assigned = true });
  }


}
